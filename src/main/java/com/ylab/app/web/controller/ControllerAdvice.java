package com.ylab.app.web.controller;

import com.ylab.app.exception.dbException.AccessDeniedException;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.exception.resourceException.ResourceNotFoundException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

/**
 * ControllerAdvice class handles exception handling for the application's controllers.
 *
 * @author razlivinsky
 * @since 02.05.2024
 */
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * Handles the exception when a requested resource is not found.
     *
     * @param e the ResourceNotFoundException instance
     * @return the response entity with a not found status and the exception's message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the exception when there is a user validation error.
     *
     * @param e the UserValidationException instance
     * @return the response with a bad request status and the exception's message
     */
    @ExceptionHandler(UserValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUserValidation(UserValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions related to workout operations.
     *
     * @param e the WorkoutException instance
     * @return the response entity with a bad request status and the exception's message
     */
    @ExceptionHandler(WorkoutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleWorkout(WorkoutException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception when access to a resource is denied.
     *
     * @return the response entity with a forbidden status and an "Access denied" message
     */
    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDenied() {
        return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
    }

    /**
     * Handles exceptions related to database write operations.
     *
     * @return the response entity with an internal server error status and an "Error database write" message
     */
    @ExceptionHandler(DatabaseWriteException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleDatabaseWriteException() {
        return new ResponseEntity<>("Error database write", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions related to database read operations.
     *
     * @return the response entity with an internal server error status and an "Error database read" message
     */
    @ExceptionHandler(DatabaseReadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleDatabaseReadException() {
        return new ResponseEntity<>("Error database read", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions related to date and time parsing.
     *
     * @param e the DateTimeParseException instance
     * @return the response entity with a bad request status and a descriptive message about the invalid date format
     */
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return new ResponseEntity<>("Invalid date format: " + e.getMessage() + " required date format 2024-01-01 22:00", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions related to malformed HTTP request bodies.
     *
     * @param e the HttpMessageNotReadableException instance
     * @return the response entity with a bad request status and a message indicating a malformed request body
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("Malformed request body: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions related to invalid method arguments.
     *
     * @param e the MethodArgumentNotValidException instance
     * @return the response entity with a bad request status and a message indicating invalid data
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("Invalid data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
