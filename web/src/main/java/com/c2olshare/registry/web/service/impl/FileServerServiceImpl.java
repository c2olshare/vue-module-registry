package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.util.ContentTypeUtils;
import com.c2olshare.registry.web.entity.Module;
import com.c2olshare.registry.web.entity.Namespace;
import com.c2olshare.registry.web.service.FileServerService;
import com.c2olshare.registry.web.service.ModuleService;
import com.c2olshare.registry.web.service.NamespaceService;
import com.c2olshare.registry.web.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;

/**
 * @author MaJing
 */
@Service
public class FileServerServiceImpl implements FileServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServerServiceImpl.class);

    @Value("${server.error.path:${error.path:/error}}")
    private String errorUrl;

    private final NamespaceService namespaceService;

    private final ModuleService moduleService;

    private final StorageService storageService;

    public FileServerServiceImpl(NamespaceService namespaceService,
                                 ModuleService moduleService,
                                 StorageService storageService) {
        this.namespaceService = namespaceService;
        this.moduleService = moduleService;
        this.storageService = storageService;
    }

    @Override
    public void resolveFile(String namespaceCode, String moduleCode, String packageVersion, String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 先查询命名空间和模块，确保访问的路径合法
            Namespace namespace = namespaceService.findByCode(namespaceCode);
            Module module = moduleService.findByCode(moduleCode, namespaceCode);
            String[] prefixPaths = new String[]{namespace.getCode(), module.getCode(), packageVersion};
            String[] suffixPaths = StringUtils.splitByWholeSeparatorPreserveAllTokens(path, "/");
            String[] fullPaths = ArrayUtils.addAll(prefixPaths, suffixPaths);
            File file = storageService.get(fullPaths);
            if (Objects.nonNull(file)) {
                if (file.isDirectory()) {
                    handleError(HttpServletResponse.SC_NOT_FOUND, request, response);
                } else {
                    writeFile(file, request, response);
                }
            } else {
                handleError(HttpServletResponse.SC_NOT_FOUND, request, response);
            }
        } catch (EmptyResultDataAccessException e) {
            handleError(HttpServletResponse.SC_NOT_FOUND, request, response);
        } catch (Exception e) {
            handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request, response);
        }
    }

    private void handleError(int statsCode, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("javax.servlet.error.status_code", statsCode);
            request.getRequestDispatcher(errorUrl).forward(request, response);
        } catch (Exception e) {
            LOGGER.warn("Forward to error controller failed", e);
            response.setStatus(statsCode);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            response.setContentLength(0);
        }
    }

    private void writeFile(File file, HttpServletRequest request, HttpServletResponse response) {
        String fileName = file.getName();

        response.setContentLengthLong(file.length());
        response.setContentType(ContentTypeUtils.valueOf(FilenameUtils.getExtension(fileName)));
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("Last-Modified", String.valueOf(file.lastModified()));

        try (ServletOutputStream outputStream = response.getOutputStream();
             InputStream inputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = bufferedInputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request, response);
        }
    }
}
