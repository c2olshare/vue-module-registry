package com.c2olshare.registry.web.controller;

import com.c2olshare.registry.common.base.BaseController;
import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.entity.Namespace;
import com.c2olshare.registry.web.service.NamespaceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaJing
 */
@RestController
@RequestMapping(UrlPrefix.NAMESPACE)
public class NamespaceController extends BaseController<Namespace, String> {

    private final NamespaceService namespaceService;

    public NamespaceController(NamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    @Override
    protected BaseService<Namespace, String> baseService() {
        return namespaceService;
    }

    @PreAuthorize("hasPermission(namespaceCode, 'namespace', 'read')")
    @GetMapping("/code/{namespaceCode}")
    public Namespace getByCode(@PathVariable("namespaceCode") String namespaceCode) {
        return namespaceService.findByCode(namespaceCode);
    }
}
