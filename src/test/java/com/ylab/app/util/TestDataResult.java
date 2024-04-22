package com.ylab.app.util;

import static com.ylab.app.util.TestDataReader.getDatabaseTestData;

/**
 * TestDataResult class provides methods to retrieve test data related to the database for testing purposes.
 *
 * @author razlivinsky
 * @since 15.04.2024
 */
public class TestDataResult {
    private static final String TEST_DATABASE_NAME = "database";
    private static final String TEST_USERNAME = "user";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_DATABASE_VERSION = "version";

    /**
     * Retrieves the test database name.
     *
     * @return the test database name
     */
    public static String getTestDatabaseName() {
        return getDatabaseTestData(TEST_DATABASE_NAME);
    }

    /**
     * Retrieves the test username.
     *
     * @return the test username
     */
    public static String getTestUsername() {
        return getDatabaseTestData(TEST_USERNAME);
    }

    /**
     * Retrieves the test password.
     *
     * @return the test password
     */
    public static String getTestPassword() {
        return getDatabaseTestData(TEST_PASSWORD);
    }

    /**
     * Retrieves the test database version.
     *
     * @return the test database version
     */
    public static String getTestDatabaseVersion() {
        return getDatabaseTestData(TEST_DATABASE_VERSION);
    }
}