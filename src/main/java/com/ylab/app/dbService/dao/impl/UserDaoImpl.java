package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.dbService.mappers.UserRowMapper;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static com.ylab.app.util.DataResultUserQuery.*;

/**
 * The UserDaoImpl class provides methods for data access related to users in the database.
 * It includes methods for inserting a new user, finding a user by name and password, and getting a list of all users.
 *
 * @author razlivinsky
 * @since 02.05.2024
 */
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    /**
     * Inserts the provided user into the database.
     *
     * @param user the user object to be inserted
     * @throws DatabaseWriteException if an error occurs while interacting with the database
     */
    @Override
    public void insertUser(User user) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertUserQuery(), new String[] {"id"});
                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole().name());
                return ps;
            }, keyHolder);
            user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        } catch (DataAccessException e) {
            throw new DatabaseWriteException("Failed to insert user", e);
        }
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return a list of all users in the database
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    @Override
    public List<User> getAllUsers() {
        try {
            return jdbcTemplate.query(getListUsersQuery(), userRowMapper);
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve all users", e);
        }
    }

    /**
     * Finds a user in the database by their id.
     *
     * @param id the id of the user
     * @return the user with the specified id, or null if no such user is found
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    @Override
    public User findUserById(long id) {
        try {
            return jdbcTemplate.queryForObject(getFindUserById(), userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve user by id", e);
        }
    }

    /**
     * Retrieves a user from the database by their login name.
     *
     * @param login the login name of the user
     * @return the user with the specified login name, or null if no such user is found
     * @throws DatabaseReadException if an error occurs while interacting with the database
     */
    @Override
    public User getUserByLogin(String login) {
        try {
            return jdbcTemplate.queryForObject(getFindUserByLogin(), userRowMapper, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Failed to retrieve user by login", e);
        }
    }
}