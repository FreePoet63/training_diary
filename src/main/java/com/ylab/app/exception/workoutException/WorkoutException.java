package com.ylab.app.exception.workoutException;

/**
 * WorkoutException class documentation.
 *
 * This class extends RuntimeException to handle exceptions specific to workout operations.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class WorkoutException extends RuntimeException{

    /**
     * Constructs a new WorkoutException with the specified detail message.
     *
     * @param message the detail message.
     */
    public WorkoutException(String message) {
        super(message);
    }

    /**
     * Constructs a new WorkoutException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     */
    public WorkoutException(String message, Throwable cause) {
        super(message, cause);
    }
}