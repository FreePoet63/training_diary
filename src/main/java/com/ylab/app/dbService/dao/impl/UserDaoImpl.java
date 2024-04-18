package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ylab.app.util.DataResultUserQuery.*;

/**
 * The UserDaoImpl class provides methods for data access related to users in the database.
 * It includes methods for inserting a new user, finding a user by name and password, and getting a list of all users.
 * @author razlivinsky
 * @since 29.01.2024
 */
public class UserDaoImpl implements UserDao {

    /**
     * Inserts the provided user into the database.
     *
     * @param user the user object to be inserted
     * @throws DatabaseWriteException if an error occurs while interacting with the database
     */
    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertUserQuery(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, String.valueOf(user.getRole()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    user.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseWriteException("Inserting user failed, no ID obtained.");
        }
    }

    /**
     * Finds a user in the database by their name and password.
     *
     * @param name the name of the user
     * @param password the password of the user
     * @return the user with the specified name and password, or null if no such user is found
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    @Override
    public User findUserByNameAndPassword(String name, String password) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getLoginQuery())) {
            stmt.setString(1, name);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserRole role = UserRole.fromString(rs.getString("role"));
                    User user = new User(
                            rs.getString("name"),
                            rs.getString("password"),
                            role);
                    user.setId(rs.getLong("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return a list of all users in the database
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getListUsersQuery());
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserRole role = UserRole.fromString(rs.getString("role"));
                User user = new User(
                        rs.getString("name"),
                        rs.getString("password"),
                        role);
                user.setId(rs.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return users;
    }
}