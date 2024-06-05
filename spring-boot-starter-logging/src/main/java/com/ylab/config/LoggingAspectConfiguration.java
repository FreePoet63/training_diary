package com.ylab.config;

import com.ylab.aspect.LoggingAspectAnnotation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Logging Aspect.
 * <p>
 * This class is used to configure the logging aspect by defining a bean LoggingAspectAnnotation.
 *
 * @author razlivinsky
 * @since 06.03.2024
 */
@Configuration
public class LoggingAspectConfiguration {
    /**
     * Creates a bean for logging aspect based on the annotation.
     *
     * @return an instance of LoggingAspectAnnotation
     */
    @Bean
    public LoggingAspectAnnotation annotation() {
        return new LoggingAspectAnnotation();
    }
}