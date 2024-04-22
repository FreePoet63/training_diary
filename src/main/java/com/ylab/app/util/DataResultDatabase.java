package com.ylab.app.util;

import static com.ylab.app.util.DataReader.getDatabaseData;

/**
 * DataResultDatabase class provides methods to retrieve database connection details from the resource bundle.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataResultDatabase {
    private static final String URL = "url";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String DATABASE_NAME = "database";

    /**
     * Retrieves the URL for the database connection.
     *
     * @return the URL for the database connection
     */
    public static String getUrl() {
        return getDatabaseData(URL);
    }

    /**
     * Retrieves the username for the database connection.
     *
     * @return the username for the database connection
     */
    public static String getUsername() {
        return getDatabaseData(USERNAME);
    }

    /**
     * Retrieves the password for the database connection.
     *
     * @return the password for the database connection
     */
    public static String getPassword() {
        return getDatabaseData(PASSWORD);
    }

    /**
     * Retrieves the name of the database.
     *
     * @return the name of the database
     */
    public static String getDatabaseName() {
        return getDatabaseData(DATABASE_NAME);
    }
}