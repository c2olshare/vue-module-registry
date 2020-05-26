package com.c2olshare.registry.web.repository;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.web.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author MaJing
 */
@Repository
public interface UserRepository extends BaseRepository<User, String> {

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return optional
     */
    Optional<User> findByUsername(String username);

}
