package com.c2olshare.registry.web.entity;

import com.c2olshare.registry.common.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author MaJing
 */
@Entity
@Table(name = "t_vmr_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = -2805811683163130190L;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}