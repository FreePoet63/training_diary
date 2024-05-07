package com.ylab.app.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JwtProperties class represents the properties related to JWT configuration.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String secret;
    private long access;
    private long refresh;
}