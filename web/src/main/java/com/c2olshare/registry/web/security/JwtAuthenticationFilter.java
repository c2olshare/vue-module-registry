package com.c2olshare.registry.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.c2olshare.registry.common.util.DateTimeUtils;
import com.c2olshare.registry.web.constant.Variables;
import com.c2olshare.registry.web.entity.User;
import com.c2olshare.registry.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Jwt认证过滤器
 *
 * @author MaJing
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String PREFIX = "Bearer";

    private static final String AUTHORIZATION = "Authorization";

    private final UserService userService;

    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader(AUTHORIZATION);
            if (StringUtils.isNotBlank(authorization) && authorization.startsWith(PREFIX)) {
                String token = StringUtils.trim(StringUtils.remove(authorization, PREFIX));
                DecodedJWT decodedJwt = JWT.decode(token);
                String username = decodedJwt.getAudience().get(0);
                User user = userService.findByUsername(username);
                if (Objects.nonNull(user)) {
                    // 校验签名
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                    jwtVerifier.verify(token);

                    // 创建凭证
                    SimpleUserDetails simpleUserDetails = new SimpleUserDetails(user.getUsername(), user.getPassword());
                    List<? extends GrantedAuthority> authorities = new ArrayList<>();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(simpleUserDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 下发新Token
                    LocalDateTime expireTime = DateTimeUtils.transferToLocalDateTime(decodedJwt.getExpiresAt());
                    LocalDateTime updateTime = expireTime.minusMinutes(Variables.DEFAULT_TOKEN_EXPIRE * 2 / 3);
                    if (DateTimeUtils.nowTime().isAfter(updateTime)) {
                        String jwt = JWT.create()
                                .withAudience(user.getUsername())
                                .withExpiresAt(DateTimeUtils.transferToDate(LocalDateTime.now().plusMinutes(Variables.DEFAULT_TOKEN_EXPIRE)))
                                .sign(Algorithm.HMAC256(user.getPassword()));

                        response.addHeader(Variables.NEW_TOKEN_HEADER, jwt);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("解析JWT异常, {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
