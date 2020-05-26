package com.c2olshare.registry.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.c2olshare.registry.common.util.DateTimeUtils;
import com.c2olshare.registry.common.util.HttpUtils;
import com.c2olshare.registry.web.constant.Variables;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author MaJing
 */
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SimpleUserDetails principal = (SimpleUserDetails) authentication.getPrincipal();

        String jwt = JWT.create()
                .withAudience(principal.getUsername())
                .withExpiresAt(DateTimeUtils.transferToDate(LocalDateTime.now().plusMinutes(Variables.DEFAULT_TOKEN_EXPIRE)))
                .sign(Algorithm.HMAC256(principal.getPassword()));

        AuthenticationResult result = new AuthenticationResult()
                .setMessage("认证成功")
                .setToken(jwt);
        HttpUtils.responseAsJson(response, 200, result);
    }
}
