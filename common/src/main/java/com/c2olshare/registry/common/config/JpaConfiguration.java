package com.c2olshare.registry.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author MaJing
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfiguration {
}
