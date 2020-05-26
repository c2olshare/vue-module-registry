package com.c2olshare.registry.web.repository;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.web.entity.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author MaJing
 */
@Repository
public interface ModuleRepository extends BaseRepository<Module, String> {
    /**
     * 根据名称查询
     *
     * @param name          名称
     * @param namespaceCode 命名空间
     * @return optional
     */
    Optional<Module> findByNameAndNamespaceCode(String name, String namespaceCode);

    /**
     * 根据编码查询
     *
     * @param code          编码
     * @param namespaceCode 命名空间
     * @return optional
     */
    Optional<Module> findByCodeAndNamespaceCode(String code, String namespaceCode);

    /**
     * 根据命名空间查询
     *
     * @param namespaceCode 命名空间
     * @return list
     */
    List<Module> findAllByNamespaceCode(String namespaceCode);
}
