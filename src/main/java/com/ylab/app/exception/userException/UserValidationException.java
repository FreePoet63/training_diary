package com.ylab.app.exception.userException;

/**
 * UserValidationException class documentation.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class UserValidationException extends RuntimeException{

    /**
     * Constructs a new UserValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public UserValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserValidationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}