package com.c2olshare.registry.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author MaJing
 */
@SpringBootApplication
@ComponentScan(basePackages = RegistryApplication.BASE_PACKAGE)
@EntityScan(basePackages = RegistryApplication.BASE_PACKAGE)
@EnableJpaRepositories(basePackages = RegistryApplication.BASE_PACKAGE)
public class RegistryApplication {

    public static final String BASE_PACKAGE = "com.c2olshare.registry";

    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }

}
