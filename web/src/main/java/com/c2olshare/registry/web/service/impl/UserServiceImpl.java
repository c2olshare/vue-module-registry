package com.c2olshare.registry.web.service.impl;

import com.c2olshare.registry.common.base.BaseRepository;
import com.c2olshare.registry.common.base.BaseServiceImpl;
import com.c2olshare.registry.web.entity.User;
import com.c2olshare.registry.web.repository.UserRepository;
import com.c2olshare.registry.web.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author MaJing
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected BaseRepository<User, String> baseRepository() {
        return userRepository;
    }


    @Override
    public User findByUsername(String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        return optional.orElse(null);
    }
}
