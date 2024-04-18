package com.ylab.app.util;

import java.util.ResourceBundle;

/**
 * TestDataReader class provides methods to retrieve test data from the resource bundle for database testing purposes.
 *
 * @author razlivinsky
 * @since 15.04.2024
 */
public class TestDataReader {
    private static ResourceBundle resourceBundleDatabaseTestData = ResourceBundle.getBundle("test");

    /**
     * Retrieves the database test data based on the provided key.
     *
     * @param key the key to retrieve the test data
     * @return the test data string associated with the key
     */
    public static String getDatabaseTestData(String key){
        return resourceBundleDatabaseTestData.getString(key);
    }
}