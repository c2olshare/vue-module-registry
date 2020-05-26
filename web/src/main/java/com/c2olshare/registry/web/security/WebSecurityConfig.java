package com.c2olshare.registry.web.security;

import com.c2olshare.registry.web.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author MaJing
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        return new SimpleAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new SimpleAuthenticationEntryPoint();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userService);
    }

    @Bean
    public AuthenticationFailureHandler simpleAuthenticationFailureHandler() {
        return new SimpleAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler simpleAuthenticationSuccessHandler() {
        return new SimpleAuthenticationSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandler simpleLogoutSuccessHandler() {
        return new SimpleLogoutSuccessHandler();
    }

    @Bean
    public UserDetailsService simpleUserDetailsServiceImpl() {
        return new SimpleUserDetailsServiceImpl(userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(simpleUserDetailsServiceImpl())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 权限配置
                .authorizeRequests()
                // 公开接口允许访问
                .antMatchers("/api/v1/open/**").permitAll()
                // 文件服务允许访问
                .antMatchers("/vmr/content/**").permitAll()
                // 后端接口需要授权
                .antMatchers("/vmr/**").authenticated()
                // 静态资源允许访问
                .antMatchers("/", "/static/**", "/public/**", "/css/**", "/fonts/**", "/img/**", "/js/**", "/favicon.ico").permitAll()
                // 其他请求匿名访问
                .anyRequest().anonymous().and()
                // 表单登录
                .formLogin()
                .loginProcessingUrl("/vmr/auth/login").permitAll()
                .successHandler(simpleAuthenticationSuccessHandler())
                .failureHandler(simpleAuthenticationFailureHandler()).and()
                .logout().logoutUrl("/vmr/auth/logout").permitAll()
                .logoutSuccessHandler(simpleLogoutSuccessHandler()).and()
                // Jwt认证过滤器
                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 禁用Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 无权限处理
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler()).and()
                // 认证失败处理
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint()).and()
                // 禁用CSRF
                .csrf().disable();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}