package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.common.base.BaseServiceImpl;
import com.c2olshare.registry.common.constant.ErrorCode;
import com.c2olshare.registry.common.exception.BusinessException;
import com.c2olshare.registry.web.entity.Namespace;
import com.c2olshare.registry.web.repository.NamespaceRepository;
import com.c2olshare.registry.web.service.ModuleService;
import com.c2olshare.registry.web.service.NamespaceService;
import com.c2olshare.registry.web.service.StorageService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author MaJing
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NamespaceServiceImpl extends BaseServiceImpl<Namespace, String> implements NamespaceService {

    private final NamespaceRepository namespaceRepository;

    private final ModuleService moduleService;

    private final StorageService storageService;

    public NamespaceServiceImpl(NamespaceRepository namespaceRepository, ModuleService moduleService, StorageService storageService) {
        this.namespaceRepository = namespaceRepository;
        this.moduleService = moduleService;
        this.storageService = storageService;
    }

    @Override
    protected BaseRepository<Namespace, String> baseRepository() {
        return namespaceRepository;
    }

    @Override
    public Namespace save(Namespace namespace) {
        Optional<Namespace> optionalByName = namespaceRepository.findByName(namespace.getName());
        if (optionalByName.isPresent()) {
            throw new BusinessException(ErrorCode.NAME_ALREADY_EXIST);
        }

        Optional<Namespace> optionalByCode = namespaceRepository.findByCode(namespace.getCode());
        if (optionalByCode.isPresent()) {
            throw new BusinessException(ErrorCode.CODE_ALREADY_EXIST);
        }

        namespace.setPreset(Boolean.FALSE);
        return super.save(namespace);
    }

    @Override
    public void deleteById(String s) {
        Namespace namespace = this.findById(s);
        moduleService.deleteByNamespaceCode(namespace.getCode());
        super.deleteById(s);
        // {命名空间}
        storageService.delete(namespace.getCode());
    }

    @Override
    public Namespace findByCode(String code) {
        Optional<Namespace> optional = namespaceRepository.findByCode(code);
        return optional.orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}
