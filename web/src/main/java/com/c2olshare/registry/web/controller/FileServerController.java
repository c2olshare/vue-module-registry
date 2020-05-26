package com.c2olshare.registry.web.controller;

import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.constant.Variables;
import com.c2olshare.registry.web.service.FileServerService;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author MaJing
 */
@Controller
@RequestMapping(UrlPrefix.CONTENT)
public class FileServerController {

    private final FileServerService fileServerService;

    public FileServerController(FileServerService fileServerService) {
        this.fileServerService = fileServerService;
    }

    @GetMapping("/{module}/{version}/**")
    public void get(@PathVariable("module") String module,
                    @PathVariable("version") String version,
                    @RequestParam(value = "namespace", defaultValue = Variables.DEFAULT_NAMESPACE_CODE) String namespace,
                    HttpServletRequest request, HttpServletResponse response) {
        String path = extractPathFromPattern(request);
        fileServerService.resolveFile(namespace, module, version, path, request, response);
    }

    private String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
