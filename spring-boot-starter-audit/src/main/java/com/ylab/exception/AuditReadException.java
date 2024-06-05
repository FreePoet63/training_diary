package com.ylab.exception;

/**
 * Exception thrown when an error occurs during the retrieval of audit messages.
 *
 * This exception is typically used when there is a failure in retrieving audit messages from the data store.
 *
 * @author razlivinsky
 * @since 14.05.2024
 */
public class AuditReadException extends RuntimeException{

    /**
     * Instantiates a new Audit read exception with the specified error message.
     *
     * @param message the detail message
     */
    public AuditReadException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Audit read exception with the specified error message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public AuditReadException(String message, Throwable cause) {
        super(message, cause);
    }
}