package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.session.Session;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a service for managing users in the system.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    /**
     * Instantiates a new user service with an empty user map.
     */
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    /**
     * Registers a new user with the given name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user ("user" or "admin")
     * @throws UserValidationException  if the name, password, or role is invalid
     */
    @Override
    public void registerUser(String name, String password, UserRole role) {
        validateFromUserNameAndPassword(name, password);
        if (role == null || !(role.equals(UserRole.USER) || role.equals(UserRole.ADMIN))) {
            throw new UserValidationException("Invalid role");
        }
        User user = new User(name, password, UserRole.USER);
        try {
            userDao.insertUser(user);
        } catch (SQLException e) {
            throw new UserValidationException("Problem registration " + e.getMessage());
        }
    }

    /**
     * Logs in a user with the provided name and password.
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws UserValidationException  if the name or password is invalid
     */
    @Override
    public User loginUser(String name, String password) {
        validateFromUserNameAndPassword(name, password);
        try {
            User user = userDao.findUserByNameAndPassword(name, password);
            if (user == null) {
                throw new UserValidationException("Invalid credentials");
            }
            Session session = Session.getInstance();
            session.setUser(user);
            return user;
        } catch (SQLException e) {
            throw new UserValidationException("An error occurred while logging in. Please try again. " + e.getMessage());
        }
    }

    /**
     * Checks if the user has admin role.
     *
     * @param user the user to check
     * @return true if the user has admin role, false otherwise
     * @throws UserValidationException if the user is null
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
        try {
            return new ArrayList<>(userDao.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserValidationException("An error occurred while retrieving users. Please try again.");
        }
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