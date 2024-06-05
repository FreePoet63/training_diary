package com.ylab.app.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

/**
 * The DataReader class provides methods to retrieve data from resource bundles for database queries,
 * liquibase migration, user queries, and workout queries.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataReader {
    public static final String USER_QUERY = "user_query.yml";
    public static final String WORKOUT_QUERY = "workout_query.yml";

    /**
     * Reads the user query string specified by the value.
     *
     * @param value the key for the user query string
     * @return the user query string
     */
    public static String readUserQuery(String value) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(USER_QUERY));
        return Objects.requireNonNull(yaml.getObject()).getProperty(value);
    }


    /**
     * Reads the workout query string specified by the value.
     *
     * @param value the key for the workout query string
     * @return the workout query string
     */
    public static String readWorkoutQuery(String value) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(WORKOUT_QUERY));
        return Objects.requireNonNull(yaml.getObject()).getProperty(value);
    }
}