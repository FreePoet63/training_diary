package com.ylab.exception;

/**
 * Exception thrown when an error occurs during the writing of audit messages.
 *
 * This exception is typically used when there is a failure in writing audit messages to the data store.
 *
 * @author razlivinsky
 * @since 14.05.2024
 */
public class AuditWriteException extends RuntimeException{

    /**
     * Instantiates a new Audit write exception with the specified error message.
     *
     * @param message the detail message
     */
    public AuditWriteException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Audit write exception with the specified error message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public AuditWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}