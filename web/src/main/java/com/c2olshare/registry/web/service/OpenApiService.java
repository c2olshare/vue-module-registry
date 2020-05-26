package com.c2olshare.registry.web.service;

import com.c2olshare.registry.web.dto.ModuleDTO;
import com.c2olshare.registry.web.dto.PackageDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author MaJing
 */
public interface OpenApiService {

    /**
     * 获取模块信息
     *
     * @param moduleCode    模块代码
     * @param namespaceCode 命名空间代码
     * @return module
     */
    ModuleDTO findByCode(String moduleCode, String namespaceCode);
}
