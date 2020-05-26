package com.c2olshare.registry.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * BaseRepository
 *
 * @author MaJing
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends String> extends JpaRepository<T, ID> {
}

