package com.ylab.app.exception.dbException;

/**
 * The DatabaseConnectionException class represents an exception that is thrown when a database connection error occurs.
 * It extends the RuntimeException class, making it an unchecked exception.
 * @author razlivinsky
 * @since 03.02.2024
 */
public class DatabaseConnectionException extends RuntimeException {

    /**
     * Constructs a new database connection exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new database connection exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}