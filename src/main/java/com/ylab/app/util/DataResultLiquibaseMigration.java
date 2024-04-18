package com.ylab.app.util;

import static com.ylab.app.util.DataReader.getLiquibaseMigration;

/**
 * DataResultLiquibaseMigration class provides methods to retrieve Liquibase migration details from the resource bundle.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataResultLiquibaseMigration {
    private static final String DEFAULT_SCHEMA_NAME = "liquibase.defaultSchemaName";

    /**
     * Retrieves the default schema name for Liquibase migration.
     *
     * @return the default schema name for Liquibase migration
     */
    public static String getDefaultSchemaName() {
        return getLiquibaseMigration(DEFAULT_SCHEMA_NAME);
    }
}