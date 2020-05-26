package com.c2olshare.registry.web.config;

import com.c2olshare.registry.web.filter.RequestUriHolderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author MaJing
 */
@Configuration
public class WebConfiguration {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public RequestUriHolderFilter requestUriHolderFilter() {
        return new RequestUriHolderFilter();
    }

    @Bean
    public FilterRegistrationBean<RequestUriHolderFilter> registerRequestUriHolderFilter(RequestUriHolderFilter filter) {
        FilterRegistrationBean<RequestUriHolderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setName(RequestUriHolderFilter.class.getName());
        registration.addUrlPatterns("/*");
        registration.setOrder(-100);
        return registration;
    }

}
