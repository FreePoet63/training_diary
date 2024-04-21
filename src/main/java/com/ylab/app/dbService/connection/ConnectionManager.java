package com.ylab.app.dbService.connection;

import com.ylab.app.exception.dbException.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.ylab.app.util.DataResultDatabase.*;

/**
 * The ConnectionManager class provides methods for creating and managing database connections.
 * It contains a static method for obtaining a connection to the database.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class ConnectionManager {
    private static Connection connectionOverride = null;
    private static String containerUrl;
    private static String containerUsername;
    private static String containerPassword;

    /**
     * Sets the connection override for the ConnectionManager.
     *
     * @param connection the connection to be set as the override
     */
    public static void setConnectionOverride(Connection connection) {
        connectionOverride = connection;
    }

    /**
     * Sets the credentials for connecting to a containerized instance of a database.
     *
     * @param url      the JDBC URL of the containerized database instance
     * @param username the username for the database connection
     * @param password the password for the database connection
     */
    public static void setContainerCredentials(String url, String username, String password) {
        containerUrl = url;
        containerUsername = username;
        containerPassword = password;
    }

    /**
     * Gets a Connection object for the database.
     *
     * @return a Connection object to interact with the database
     * @throws DatabaseConnectionException if an error occurs while attempting to connect to the database
     */
    public static Connection getConnection() {
        if (connectionOverride != null) {
            try {
                if (connectionOverride.isClosed()) {
                    connectionOverride = DriverManager.getConnection(containerUrl, containerUsername, containerPassword);
                }
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Connection override is invalid or closed: " + e.getMessage());
            }
            return connectionOverride;
        } else {
            try {
                return DriverManager.getConnection(getUrl() + getDatabaseName(), getUsername(), getPassword());
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Unable to create a new database connection: " + e.getMessage());
            }
        }
    }
}