package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.common.base.BaseServiceImpl;
import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.exception.BusinessException;
import com.c2olshare.registry.web.entity.Module;
import com.c2olshare.registry.web.repository.ModuleRepository;
import com.c2olshare.registry.web.service.ModuleService;
import com.c2olshare.registry.web.service.PackageService;
import com.c2olshare.registry.web.service.StorageService;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author MaJing
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ModuleServiceImpl extends BaseServiceImpl<Module, String> implements ModuleService {

    public static final String EMPTY = "";

    private final ModuleRepository moduleRepository;

    private final PackageService packageService;

    private final StorageService storageService;

    public ModuleServiceImpl(ModuleRepository moduleRepository,
                             @Lazy PackageService packageService,
                             StorageService storageService) {
        this.moduleRepository = moduleRepository;
        this.packageService = packageService;
        this.storageService = storageService;
    }

    @Override
    protected BaseRepository<Module, String> baseRepository() {
        return moduleRepository;
    }

    @Override
    public Module save(Module module) {
        Optional<Module> optionalByName = moduleRepository.findByNameAndNamespaceCode(module.getName(), module.getNamespaceCode());
        if (optionalByName.isPresent()) {
            throw new BusinessException(ErrorCode.NAME_ALREADY_EXIST);
        }

        Optional<Module> optionalByCode = moduleRepository.findByCodeAndNamespaceCode(module.getCode(), module.getNamespaceCode());
        if (optionalByCode.isPresent()) {
            throw new BusinessException(ErrorCode.CODE_ALREADY_EXIST);
        }

        module.setCurrent(EMPTY);
        return super.save(module);
    }

    @Override
    public Module update(String s, Module module) {
        Optional<Module> optionalByName = moduleRepository.findByNameAndNamespaceCode(module.getName(), module.getNamespaceCode());
        if (optionalByName.isPresent() && !Objects.equals(module.getId(), optionalByName.get().getId())) {
            throw new BusinessException(ErrorCode.NAME_ALREADY_EXIST);
        }

        Optional<Module> optionalByCode = moduleRepository.findByCodeAndNamespaceCode(module.getCode(), module.getNamespaceCode());
        if (optionalByCode.isPresent() && !Objects.equals(module.getId(), optionalByCode.get().getId())) {
            throw new BusinessException(ErrorCode.CODE_ALREADY_EXIST);
        }
        return super.update(s, module);
    }

    @Override
    public Module findByCode(String code, String namespaceCode) {
        Optional<Module> optional = moduleRepository.findByCodeAndNamespaceCode(code, namespaceCode);
        return optional.orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public void deleteByNamespaceCode(String namespaceCode) {
        List<Module> moduleList = moduleRepository.findAllByNamespaceCode(namespaceCode);
        for (Module module : moduleList) {
            this.deleteById(module.getId());
        }
    }

    @Override
    public void deleteById(String s) {
        Module module = this.findById(s);
        packageService.deleteByModuleCode(module.getCode(), module.getNamespaceCode());
        super.deleteById(s);
        // {命名空间}/{模块}
        storageService.delete(module.getNamespaceCode(), module.getCode());
    }
}
