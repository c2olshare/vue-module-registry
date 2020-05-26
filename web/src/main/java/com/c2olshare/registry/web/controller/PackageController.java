package com.c2olshare.registry.web.controller;

import com.c2olshare.registry.common.base.BaseController;
import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.constant.Variables;
import com.c2olshare.registry.web.dto.FileInfoDTO;
import com.c2olshare.registry.web.entity.Package;
import com.c2olshare.registry.web.service.PackageService;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author MaJing
 */
@RestController
@RequestMapping(UrlPrefix.PACKAGE)
public class PackageController extends BaseController<Package, String> {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @Override
    protected BaseService<Package, String> baseService() {
        return packageService;
    }

    @Override
    public Package save(Package tPackage) {
        throw new UnsupportedOperationException("Unknown exception");
    }

    @PostMapping("publish")
    public Package publish(@RequestParam("file") MultipartFile file,
                           @RequestParam(value = "active", defaultValue = "false") Boolean active,
                           @Validated Package tPackage) {
        return packageService.publish(file, tPackage, active);
    }

    @GetMapping("/files/{moduleCode}/{packageVersion}/**")
    public List<FileInfoDTO> get(@PathVariable("moduleCode") String moduleCode,
                                 @PathVariable("packageVersion") String packageVersion,
                                 @RequestParam(value = "namespaceCode", defaultValue = Variables.DEFAULT_NAMESPACE_CODE) String namespaceCode,
                                 HttpServletRequest request) {
        String filePath = extractPathFromPattern(request);
        return packageService.listFile(namespaceCode, moduleCode, packageVersion, filePath);
    }

    private String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
