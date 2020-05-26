package com.c2olshare.registry.web.service;

import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.entity.Namespace;

/**
 * @author MaJing
 */
public interface NamespaceService extends BaseService<Namespace, String> {
    Namespace findByCode(String code);
}
