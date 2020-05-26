package com.c2olshare.registry.web.security;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author MaJing
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResult {

    private String message;

    private String token;

    public String getMessage() {
        return message;
    }

    public AuthenticationResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getToken() {
        return token;
    }

    public AuthenticationResult setToken(String token) {
        this.token = token;
        return this;
    }
}
