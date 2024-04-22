package com.ylab.app.exception.dbException;

/**
 * The DatabaseWriteException class represents an exception that is thrown when an error occurs during database write operations.
 * It extends the RuntimeException class, making it an unchecked exception.
 * @author razlivinsky
 * @since 03.02.2024
 */
public class DatabaseWriteException extends RuntimeException {
    /**
     * Constructs a new write exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public DatabaseWriteException(String message) {
        super(message);
    }

    /**
     * Constructs a new database write exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public DatabaseWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}