package com.ylab.app.exception.dbException;

/**
 * LiquibaseMigrationException class represents an exception that occurs during Liquibase migrations.
 * It extends the RuntimeException class.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class LiquibaseMigrationException extends RuntimeException{

    /**
     * Constructs a new LiquibaseMigrationException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public LiquibaseMigrationException(String message) {
        super(message);
    }

    /**
     * Constructs a new LiquibaseMigrationException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public LiquibaseMigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}