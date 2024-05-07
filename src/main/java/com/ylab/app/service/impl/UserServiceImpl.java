package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.resourceException.ResourceNotFoundException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a service for managing users in the system.
 * This service provides operations related to user registration, role validation, and user retrieval.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the given name and password, assigning the role as USER.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @return the newly registered user
     * @throws UserValidationException if the name or password is invalid
     */
    @Override
    public User registerUser(String name, String password) {
        validateFromUserNameAndPassword(name, password);
        User user = new User(name, passwordEncoder.encode(password), UserRole.USER);
        userDao.insertUser(user);
        return user;
    }

    /**
     * Checks if the user has an admin role.
     *
     * @param user the user to check
     * @return true if the user has an admin role, false otherwise
     */
    @Override
    public boolean hasRoleAdmin(User user) {
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
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userDao.getAllUsers());
    }

    /**
     * Finds a user in the system by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID, or null if no such user is found
     * @throws ResourceNotFoundException if the user ID is invalid
     */
    @Override
    public User getUserById(long id) {
        User user = userDao.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        return user;
    }

    /**
     * Finds a user in the system by their login.
     *
     * @param login the login (username) of the user to find
     * @return the user with the specified login, or null if no such user is found
     * @throws ResourceNotFoundException if the user with the given login is not found
     */
    @Override
    public User getUserByLogin(String login) {
        User user = userDao.getUserByLogin(login);
        if (user.getName() == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        return user;
    }

    /**
     * Validates the username and password for any potential issues.
     *
     * @param name     the username to be validated
     * @param password the password to be validated
     * @throws UserValidationException if the username or password is invalid
     */
    private void validateFromUserNameAndPassword(String name, String password) {
        if (name == null || name.isEmpty()) {
            throw new UserValidationException("Invalid credentials");
        }
        if (password == null || password.isEmpty()) {
            throw new UserValidationException("Invalid credentials");
        }
    }
}