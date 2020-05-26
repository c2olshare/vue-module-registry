package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.util.FileUtils;
import com.c2olshare.registry.web.config.SysProperties;
import com.c2olshare.registry.web.service.StorageService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author MaJing
 */
@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final String root;

    public StorageServiceImpl(SysProperties properties) {
        String home = properties.getHome();
        if (StringUtils.isEmpty(home)) {
            throw new RuntimeException("The root directory is not configured");
        }
        this.root = FileUtils.concat(properties.getHome(), "data", "file");
    }


    @Override
    public File save(MultipartFile file, String... paths) {
        String target = FileUtils.concat(FileUtils.concat(root, paths), file.getOriginalFilename());
        try {
            File targetFile = new File(target);
            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
            return targetFile;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("保存文件保存失败");
        }
    }

    @Override
    public void delete(String... paths) {
        String target = FileUtils.concat(FileUtils.concat(root, paths));
        File targetFile = new File(target);
        if (targetFile.exists()) {
            if (!FileUtils.deleteQuietly(targetFile)) {
                throw new RuntimeException("删除文件失败");
            }
        }
    }

    @Override
    public File get(String... paths) {
        String target = FileUtils.concat(FileUtils.concat(root, paths));
        File targetFile = new File(target);
        return targetFile.exists() ? targetFile : null;
    }

    @Override
    public File[] list(String... paths) {
        File file = get(paths);
        return Objects.nonNull(file) ? file.listFiles() : null;
    }
}
