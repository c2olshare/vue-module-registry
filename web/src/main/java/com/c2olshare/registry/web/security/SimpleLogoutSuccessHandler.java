package com.c2olshare.registry.web.security;

import com.c2olshare.registry.common.util.HttpUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MaJing
 */
public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthenticationResult result = new AuthenticationResult()
                .setMessage("退出成功");
        HttpUtils.responseAsJson(response, 200, result);
    }
}
