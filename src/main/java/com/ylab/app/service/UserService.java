package com.ylab.app.service;

import com.ylab.app.model.user.User;

import java.util.List;

/**
 * An interface for managing users in the system.
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
public interface UserService {

    /**
     * Registers a new user with the provided name and password.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @return the newly registered User object
     */
    User registerUser(String name, String password);

    /**
     * Checks if the user has the admin role.
     *
     * @param user the user to check
     * @return true if the user has admin role, false otherwise
     */
    boolean hasRoleAdmin(User user);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return the list of all users
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user
     * @return the user corresponding to the given ID
     */
    User getUserById(long userId);

    /**
     * Retrieves a user by their login.
     *
     * @param login the login name of the user
     * @return the user corresponding to the given login name
     */
    User getUserByLogin(String login);
}