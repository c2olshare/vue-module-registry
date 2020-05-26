package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.common.base.BaseServiceImpl;
import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.exception.BusinessException;
import com.c2olshare.registry.common.util.FileUtils;
import com.c2olshare.registry.common.util.HttpUtils;
import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.dto.FileInfoDTO;
import com.c2olshare.registry.web.entity.Module;
import com.c2olshare.registry.web.entity.Namespace;
import com.c2olshare.registry.web.entity.Package;
import com.c2olshare.registry.web.repository.PackageRepository;
import com.c2olshare.registry.web.service.ModuleService;
import com.c2olshare.registry.web.service.NamespaceService;
import com.c2olshare.registry.web.service.PackageService;
import com.c2olshare.registry.web.service.StorageService;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author MaJing
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PackageServiceImpl extends BaseServiceImpl<Package, String> implements PackageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageServiceImpl.class);

    public static final String SUFFIX = ".umd.min.js";

    private final PackageRepository packageRepository;

    private final ModuleService moduleService;

    private final NamespaceService namespaceService;

    private final StorageService storageService;

    public PackageServiceImpl(PackageRepository packageRepository,
                              @Lazy ModuleService moduleService,
                              NamespaceService namespaceService,
                              StorageService storageService) {
        this.packageRepository = packageRepository;
        this.moduleService = moduleService;
        this.namespaceService = namespaceService;
        this.storageService = storageService;
    }

    @Override
    protected BaseRepository<Package, String> baseRepository() {
        return packageRepository;
    }

    @Override
    public Package save(Package aPackage) {
        Optional<Package> optional = packageRepository.findByVersionAndModuleCodeAndNamespaceCode(
                aPackage.getVersion(), aPackage.getModuleCode(), aPackage.getNamespaceCode()
        );
        if (optional.isPresent()) {
            throw new BusinessException(ErrorCode.VERSION_ALREADY_EXIST);
        }

        return super.save(aPackage);
    }

    @Override
    public void deleteById(String id) {
        Optional<Package> optional = packageRepository.findById(id);
        Package tPackage = optional.orElseThrow(() -> {
            LOGGER.error("No package entity with id {} exists!", id);
            return new BusinessException(ErrorCode.RESOURCE_NOT_EXIST);
        });
        super.deleteById(id);
        // {命名空间}/{模块}/{版本}
        storageService.delete(tPackage.getNamespaceCode(), tPackage.getModuleCode(), tPackage.getVersion());
    }

    @Override
    public Package publish(MultipartFile multipartFile, Package tPackage, boolean active) {
        // 保存模块包信息
        Package aPackage = this.save(tPackage);

        // 查询所属模块
        if (active) {
            Module module = moduleService.findByCode(tPackage.getModuleCode(), tPackage.getNamespaceCode());
            Module current = module.setCurrent(tPackage.getVersion());
            moduleService.update(current.getId(), current);
        }

        // 保存模块包文件，路径：{命名空间}/{模块}/{版本}/文件列表
        File zipFile = storageService.save(
                multipartFile, tPackage.getNamespaceCode(), tPackage.getModuleCode(), tPackage.getVersion()
        );
        File parentFile = zipFile.getParentFile();
        String packagePath = parentFile.getAbsolutePath();
        try {
            ZipFile zip = new ZipFile(zipFile);
            zip.extractAll(packagePath);
            File checkFile = new File(FileUtils.concat(packagePath, tPackage.getModuleCode() + SUFFIX));
            if (!checkFile.exists()) {
                if (!FileUtils.deleteQuietly(parentFile)) {
                    LOGGER.warn("删除模块文件失败: {}", packagePath);
                }
                throw new BusinessException("模块包内容不完整");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!FileUtils.deleteQuietly(zipFile)) {
                LOGGER.warn("删除原始文件失败: {}", packagePath);
            }
        }

        return aPackage;
    }

    @Override
    public void deleteByModuleCode(String moduleCode, String namespaceCode) {
        List<Package> packageList = packageRepository.findAllByModuleCodeAndNamespaceCode(namespaceCode, moduleCode);
        for (Package aPackage : packageList) {
            this.deleteById(aPackage.getId());
        }
    }

    @Override
    public List<FileInfoDTO> listFile(String namespaceCode, String moduleCode, String packageVersion, String path) {
        Namespace namespace = namespaceService.findByCode(namespaceCode);
        Module module = moduleService.findByCode(moduleCode, namespaceCode);

        String[] prefixPaths = new String[]{namespaceCode, moduleCode, packageVersion};
        String[] suffixPaths = StringUtils.splitByWholeSeparatorPreserveAllTokens(path, "/");
        String[] fullPaths = ArrayUtils.addAll(prefixPaths, suffixPaths);
        File[] files = storageService.list(fullPaths);
        if (Objects.isNull(files)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_EXIST);
        }

        ArrayList<FileInfoDTO> fileInfoList = new ArrayList<>();

        String baseUrl = null;
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(servletRequestAttributes)) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String basePath = HttpUtils.getHostAddress(request);
                String suffixPath = StringUtils.join(suffixPaths, "/");
                // basePath/storage/模块/版本号/路径
                baseUrl = String.format("%s/%s/%s/%s/%s",
                        basePath, UrlPrefix.CONTENT, module.getCode(), packageVersion,
                        StringUtils.isNotBlank(suffixPath) ? suffixPath + "/" : "");
            }
        } catch (Exception e) {
            LOGGER.warn("解析URL错误", e);
        }
        for (File file : files) {
            String fileName = file.getName();
            FileInfoDTO fileInfoDTO = new FileInfoDTO()
                    .setName(fileName)
                    .setLength(file.length())
                    .setModifyTime(file.lastModified())
                    .setDirectory(file.isDirectory());
            if (file.isFile()) {
                // 设置文件扩展类型
                String extension = FilenameUtils.getExtension(fileName);
                fileInfoDTO.setExtension(extension);

                // 设置URL
                if (Objects.nonNull(baseUrl)) {
                    String fileUrl = String.format("%s/%s?namespace=%s", baseUrl, fileName, namespace.getCode());
                    fileInfoDTO.setUrl(HttpUtils.sanitizeUrl(fileUrl));
                }
            }
            fileInfoList.add(fileInfoDTO);
        }
        return fileInfoList;
    }

}
