package com.c2olshare.registry.web.repository;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.web.entity.Package;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author MaJing
 */
@Repository
public interface PackageRepository extends BaseRepository<Package, String> {

    /**
     * 根据版本查询
     *
     * @param version       版本
     * @param moduleCode    模块
     * @param namespaceCode 命名空间
     * @return package
     */
    Optional<Package> findByVersionAndModuleCodeAndNamespaceCode(String version, String moduleCode, String namespaceCode);

    /**
     * 根据模块查询
     *
     * @param moduleCode    模块
     * @param namespaceCode 命名空间
     * @return list
     */
    List<Package> findAllByModuleCodeAndNamespaceCode(String moduleCode, String namespaceCode);

}
