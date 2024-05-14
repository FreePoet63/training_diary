package com.ylab.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static com.ylab.app.util.DataReader.readDatabaseConfiguration;

/**
 * DatabaseConfig class provides the configuration for the database connectivity using Spring JDBC.
 * The class reads the database configuration properties and sets up the JDBC template and data source.
 * <p>
 * It defines the database driver, URL, username, and password based on the provided configuration properties.
 *
 * @author Raz Livinsky
 * @since 13.05.2024
 */
@Configuration
public class DatabaseConfig {
    public static final String DATABASE_DRIVER = readDatabaseConfiguration("spring.datasource.driver-class-name");
    public static final String DATABASE_URL = readDatabaseConfiguration("spring.datasource.url");
    public static final String DATABASE_USERNAME = readDatabaseConfiguration("spring.datasource.username");
    public static final String DATABASE_PASSWORD = readDatabaseConfiguration("spring.datasource.password");

    /**
     * Configures and provides a {@link JdbcTemplate} instance based on the provided data source.
     *
     * @param dataSource the data source to be used by the JDBC template. Must not be {@code null}.
     * @return a configured {@link JdbcTemplate} instance for executing SQL queries against the database.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Configures and provides a {@link DataSource} instance based on the database configuration properties.
     *
     * @return a configured {@link DataSource} instance representing the database connection.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DATABASE_DRIVER);
        dataSource.setUrl(DATABASE_URL);
        dataSource.setUsername(DATABASE_USERNAME);
        dataSource.setPassword(DATABASE_PASSWORD);
        return dataSource;
    }
}