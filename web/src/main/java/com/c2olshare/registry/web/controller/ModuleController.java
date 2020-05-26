package com.c2olshare.registry.web.controller;

import com.c2olshare.registry.common.base.BaseController;
import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.constant.UrlPrefix;
import com.c2olshare.registry.web.constant.Variables;
import com.c2olshare.registry.web.entity.Module;
import com.c2olshare.registry.web.service.ModuleService;
import org.springframework.web.bind.annotation.*;

/**
 * @author MaJing
 */
@RestController
@RequestMapping(UrlPrefix.MODULE)
public class ModuleController extends BaseController<Module, String> {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Override
    protected BaseService<Module, String> baseService() {
        return moduleService;
    }

    @GetMapping("/code/{moduleCode}")
    public Module getByCode(@PathVariable("moduleCode") String moduleCode,
                            @RequestParam(value = "namespaceCode", defaultValue = Variables.DEFAULT_NAMESPACE_CODE) String namespaceCode) {
        return moduleService.findByCode(moduleCode, namespaceCode);
    }

}
