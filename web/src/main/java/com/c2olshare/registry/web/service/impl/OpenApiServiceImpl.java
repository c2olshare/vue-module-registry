package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.util.BeanUtils;
import com.c2olshare.registry.common.util.HttpUtils;
import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.dto.ModuleDTO;
import com.c2olshare.registry.web.entity.Module;
import com.c2olshare.registry.web.entity.Namespace;
import com.c2olshare.registry.web.service.ModuleService;
import com.c2olshare.registry.web.service.NamespaceService;
import com.c2olshare.registry.web.service.OpenApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author MaJing
 */
@Service
public class OpenApiServiceImpl implements OpenApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiServiceImpl.class);

    public static final String SUFFIX = ".umd.min.js";

    private final NamespaceService namespaceService;

    private final ModuleService moduleService;

    public OpenApiServiceImpl(NamespaceService namespaceService, ModuleService moduleService) {
        this.namespaceService = namespaceService;
        this.moduleService = moduleService;
    }

    @Override
    public ModuleDTO findByCode(String moduleCode, String namespaceCode) {
        Namespace namespace = namespaceService.findByCode(namespaceCode);
        Module module = moduleService.findByCode(moduleCode, namespace.getCode());
        ModuleDTO result = new ModuleDTO();
        BeanUtils.copyProperties(module, result);
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(servletRequestAttributes)) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String basePath = HttpUtils.getHostAddress(request);
                // basePath/storage/模块/版本号/文件名?命名空间
                String fileUrl = String.format("%s/%s/%s/%s/%s?namespace=%s",
                        basePath, UrlPrefix.CONTENT, moduleCode, module.getCurrent(),
                        moduleCode + SUFFIX, namespaceCode);
                result.setUrl(HttpUtils.sanitizeUrl(fileUrl));
            }
        } catch (Exception e) {
            LOGGER.warn("解析URL错误", e);
        }
        return result.setNamespace(namespace.getCode());
    }

}
