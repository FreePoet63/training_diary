package com.ylab.app.util;

import java.util.ResourceBundle;

/**
 * DataReader class provides methods to retrieve data from resource bundles for database, liquibase migration, user query, and workout query.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataReader {
    private static ResourceBundle resourceBundleDatabaseData = ResourceBundle.getBundle("database");
    private static ResourceBundle resourceBundleLiquibaseMigration = ResourceBundle.getBundle("liquibase");
    private static ResourceBundle resourceBundleUsersQuery = ResourceBundle.getBundle("user_query");
    private static ResourceBundle resourceBundleWorkoutQuery = ResourceBundle.getBundle("workout_query");
    private static ResourceBundle resourceBundleAuditQuery = ResourceBundle.getBundle("audit_query");

    /**
     * Retrieves the database data for the given key.
     *
     * @param key the key for retrieving database data
     * @return the database data corresponding to the given key
     */
    public static String getDatabaseData(String key){
        return resourceBundleDatabaseData.getString(key);
    }

    /**
     * Retrieves the liquibase migration data for the given key.
     *
     * @param key the key for retrieving liquibase migration data
     * @return the liquibase migration data corresponding to the given key
     */
    public static String getLiquibaseMigration(String key){
        return resourceBundleLiquibaseMigration.getString(key);
    }

    /**
     * Retrieves the user query data for the given key.
     *
     * @param key the key for retrieving user query data
     * @return the user query data corresponding to the given key
     */
    public static String getUserQuery(String key){
        return resourceBundleUsersQuery.getString(key);
    }

    /**
     * Retrieves the workout query data for the given key.
     *
     * @param key the key for retrieving workout query data
     * @return the workout query data corresponding to the given key
     */
    public static String getWorkoutQuery(String key){
        return resourceBundleWorkoutQuery.getString(key);
    }

    /**
     * Gets audit query.
     *
     * @param key the key
     * @return the audit query
     */
    public static String getAuditQuery(String key) {
        return resourceBundleAuditQuery.getString(key);
    }
}