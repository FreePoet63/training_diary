package com.ylab.app.exception.resourceException;

/**
 * ResourceNotFoundException class represents an exception indicating that a requested resource was not found.
 * This exception is typically thrown when a resource retrieval operation fails due to the absence of the requested resource.
 *
 * @author razlivinsky
 * @since 02.05.2024
 */
public class ResourceNotFoundException extends RuntimeException{

    /**
     * Instantiates a new ResourceNotFoundException with the specified message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new ResourceNotFoundException with the specified message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause   the cause (which is saved for later retrieval by the getCause() method)
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}