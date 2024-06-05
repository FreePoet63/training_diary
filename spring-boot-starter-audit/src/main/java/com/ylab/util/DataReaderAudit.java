package com.ylab.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

/**
 * DataReaderAudit class
 *
 * @author razlivinsky
 * @since 14.05.2024
 */
public class DataReaderAudit {
    public static final String AUDIT_QUERY = "audit_query.yml";

    /**
     * Reads the audit query string specified by the value.
     *
     * @param value the key for the audit query string
     * @return the audit query string
     */
    public static String readAuditQuery(String value) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(AUDIT_QUERY));
        return Objects.requireNonNull(yaml.getObject()).getProperty(value);
    }
}