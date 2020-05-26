package com.c2olshare.registry.web.security;

import com.c2olshare.registry.web.entity.User;
import com.c2olshare.registry.web.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

/**
 * @author MaJing
 */
public class SimpleUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public SimpleUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (Objects.nonNull(user)) {
            return new SimpleUserDetails(user.getUsername(), user.getPassword());
        }
        throw new UsernameNotFoundException(String.format("No user with username %s exists", username));
    }
}
