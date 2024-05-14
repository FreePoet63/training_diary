package com.ylab.app.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.ylab.app.util.DataReader.readDatabaseConfiguration;

/**
 * JwtProperties class represents the properties related to JWT configuration.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    public static final String SECRET = readDatabaseConfiguration("security.jwt.secret");
    public static final long ACCESS = Long.parseLong(readDatabaseConfiguration("security.jwt.access"));
    public static final long REFRESH = Long.parseLong(readDatabaseConfiguration("security.jwt.refresh"));
}