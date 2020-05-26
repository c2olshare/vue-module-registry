package com.c2olshare.registry.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author MaJing
 */
public class SimpleUserDetails implements UserDetails {

    private static final long serialVersionUID = -2991294278096491801L;

    private String username;

    private String password;

    private boolean enabled;

    private boolean locked;

    public SimpleUserDetails() {
    }

    public SimpleUserDetails(String username, String password, boolean enabled, boolean locked) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.locked = locked;
    }

    public SimpleUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.locked = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
