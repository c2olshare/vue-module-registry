package com.c2olshare.registry.web.repository;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.web.entity.Namespace;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author MaJing
 */
@Repository
public interface NamespaceRepository extends BaseRepository<Namespace, String> {

    /**
     * 根据名称查询
     *
     * @param name 名称
     * @return optional
     */
    Optional<Namespace> findByName(String name);

    /**
     * 根据编码查询
     *
     * @param code 编码
     * @return optional
     */
    Optional<Namespace> findByCode(String code);


}
