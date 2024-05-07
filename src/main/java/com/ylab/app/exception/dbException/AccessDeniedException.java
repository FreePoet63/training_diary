package com.ylab.app.exception.dbException;

/**
 * AccessDeniedException class represents an exception indicating access denial during runtime.
 * This exception is typically thrown when a user is denied access to a specific resource or operation.
 *
 * @author razlivinsky
 * @since 09.03.2024
 */
public class AccessDeniedException extends RuntimeException{

    /**
     * Instantiates a new AccessDeniedException with no specific message.
     */
    public AccessDeniedException() {
        super();
    }
}