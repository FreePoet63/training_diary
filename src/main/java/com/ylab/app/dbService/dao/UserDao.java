package com.ylab.app.dbService.dao;

import com.ylab.app.model.user.User;

import java.util.List;

/**
 * The UserDao interface provides methods for data access related to users in the database.
 * It includes methods for inserting a new user, finding a user by name and password, and getting a list of all users.
 *
 * @author razlivinsky
 * @since 03.02.2024
 */
public interface UserDao {

    /**
     * Inserts the provided user into the database.
     *
     * @param user the user object to be inserted
     */
    public void insertUser(User user);

    /**
     * Retrieves a list of all users from the database.
     *
     * @return a list of all users in the database
     */
    public List<User> getAllUsers();

    /**
     * Find a user in the database by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID, or null if no such user is found
     */
    User findUserById(long id);

    /**
     * Retrieves a user from the database based on their login.
     *
     * @param login the login (username) of the user to find
     * @return the user with the specified login, or null if no such user is found
     */
    User getUserByLogin(String login);
}
