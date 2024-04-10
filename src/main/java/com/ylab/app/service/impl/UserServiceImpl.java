package com.ylab.app.service.impl;

import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a service for managing users in the system.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class UserServiceImpl implements UserService {
    private Map<String, User> users;

    /**
     * Instantiates a new user service with an empty user map.
     */
    public UserServiceImpl() {
        this.users = new HashMap<>();
    }

    /**
     * Registers a new user with the given name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user ("user" or "admin")
     * @throws UserValidationException  if the name, password, or role is invalid
     */
    public void registerUser(String name, String password, UserRole role) {
        validateFromUserNameAndPassword(name, password);
        if (role == null || !(role.equals(UserRole.USER) || role.equals(UserRole.ADMIN))) {
            throw new UserValidationException("Invalid role");
        }
        if (users.containsKey(name)) {
            throw new UserValidationException("User already exists");
        }
        User user = new User(name, password, role);
        users.put(name, user);
    }

    /**
     * Logs in a user with the provided name and password.
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws UserValidationException  if the name or password is invalid
     */
    public User loginUser(String name, String password) {
        validateFromUserNameAndPassword(name, password);
        if (!users.containsKey(name)) {
            throw new UserValidationException("User does not exist");
        }
        User user = users.get(name);
        if (!user.getPassword().equals(password)) {
            throw new UserValidationException("Invalid credentials");
        }
        return user;
    }

    /**
     * Checks if the user has admin role.
     *
     * @param user the user to check
     * @return true if the user has admin role, false otherwise
     * @throws UserValidationException if the user is null
     */
    public boolean checkRole(User user) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (user.getRole().equals(UserRole.ADMIN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return the list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Validates the username and password for any potential issues.
     *
     * @param name     the username to be validated
     * @param password the password to be validated
     * @throws UserValidationException if the username or password is invalid
     */
    private void validateFromUserNameAndPassword(String name, String password) {
        if (name == null || name.isEmpty() || name.contains(" ")) {
            throw new UserValidationException("Invalid credentials");
        }
        if (password == null || password.isEmpty() || password.contains(" ")) {
            throw new UserValidationException("Invalid credentials");
        }
    }
}