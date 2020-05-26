package com.c2olshare.registry.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author MaJing
 */
@Configuration
@ConfigurationProperties(prefix = "vmr")
public class SysProperties {

    private String home;

    public String getHome() {
        return home;
    }

    public SysProperties setHome(String home) {
        this.home = home;
        return this;
    }
}
