package com.ylab.config;

import com.ylab.repository.AuditDao;
import com.ylab.repository.impl.AuditDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * AppConfig class represents the main configuration class for the application.
 *
 * This class includes configuration for enabling AspectJ auto proxy and component scanning.
 *
 * @author razlivinsky
 * @since 14.05.2024
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.ylab")
public class AppConfig {
}