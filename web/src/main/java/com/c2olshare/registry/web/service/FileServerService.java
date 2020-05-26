package com.c2olshare.registry.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author MaJing
 */
public interface FileServerService {

    /**
     * 解析文件
     *
     * @param namespaceCode  命名空间
     * @param moduleCode     模块名
     * @param packageVersion 版本
     * @param path           路径
     * @param request        请求
     * @param response       响应
     */
    void resolveFile(String namespaceCode, String moduleCode, String packageVersion, String path,
                     HttpServletRequest request, HttpServletResponse response);
}
