package com.c2olshare.registry.web.service;

import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.entity.Module;

/**
 * @author MaJing
 */
public interface ModuleService extends BaseService<Module, String> {

    /**
     * 根据编码查询
     *
     * @param code          编码
     * @param namespaceCode 命名空间
     * @return module
     */
    Module findByCode(String code, String namespaceCode);

    /**
     * 根据命名空间删除
     *
     * @param namespaceCode 命名空间
     */
    void deleteByNamespaceCode(String namespaceCode);
}
