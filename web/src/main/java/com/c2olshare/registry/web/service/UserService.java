package com.c2olshare.registry.web.service;

import com.c2olshare.registry.common.base.BaseService;
import com.c2olshare.registry.web.entity.User;

/**
 * @author MaJing
 */
public interface UserService extends BaseService<User, String> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return user
     */
    User findByUsername(String username);

}
