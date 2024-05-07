package com.ylab.app.service;

import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.web.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

/**
 * An interface for managing users in the system.
 *
 * @author razlivinsky
 * @since 28.01.2024
 */
public interface UserService {

    /**
     * Registers a new user with the given name, password, and role.
     *  @param name     the name of the user
     * @param password the password for the user
     * @return
     */
    User registerUser(String name, String password);

    /**
     * Logs in a user with the name and password.
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return the logged-in user
     */
    User loginUser(String name, String password);

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
     * @throws SQLException the sql exception
     */
    List<User> getAllUsers() throws SQLException;

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