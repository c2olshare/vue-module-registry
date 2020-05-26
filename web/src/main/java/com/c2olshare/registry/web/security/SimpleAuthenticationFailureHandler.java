package com.c2olshare.registry.web.security;

import com.c2olshare.registry.common.util.HttpUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MaJing
 */
public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String exName = exception.getClass().getSimpleName();
        String message;
        switch (exName) {
            case "ValidateCodeException": {
                message = "验证码错误";
                break;
            }
            case "UsernameNotFoundException": {
                message = "用户不存在";
                break;
            }
            case "LockedException": {
                message = "用户已锁定";
                break;
            }
            case "DisabledException": {
                message = "用户未激活";
                break;
            }
            case "BadCredentialsException": {
                message = "用户名或密码错误";
                break;
            }
            case "AuthenticationException": {
                message = "认证错误";
                break;
            }
            default: {
                message = "未知错误";
                break;
            }
        }

        AuthenticationResult result = new AuthenticationResult()
                .setMessage(message);
        HttpUtils.responseAsJson(response, 400, result);
    }
}
