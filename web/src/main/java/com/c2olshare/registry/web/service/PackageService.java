package com.c2olshare.registry.web.service;

import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.dto.FileInfoDTO;
import com.c2olshare.registry.web.entity.Package;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author MaJing
 */
public interface PackageService extends BaseService<Package, String> {

    /**
     * 上传模块包
     *
     * @param fPackage 包文件
     * @param tPackage 包信息
     * @param active   是否激活
     * @return package
     */
    Package publish(MultipartFile fPackage, Package tPackage, boolean active);

    /**
     * 根据模块删除
     *
     * @param moduleCode    模块
     * @param namespaceCode 命名空间
     */
    void deleteByModuleCode(String moduleCode, String namespaceCode);

    /**
     * 获取文件列表
     *
     * @param namespaceCode  命名空间
     * @param moduleCode     模块
     * @param packageVersion 版本
     * @param path           路径
     * @return list
     */
    List<FileInfoDTO> listFile(String namespaceCode, String moduleCode, String packageVersion, String path);
}
