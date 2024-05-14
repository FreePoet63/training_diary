package com.ylab.app.dbService.mappers;

import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserRowMapper class is responsible for mapping rows from a ResultSet to User instances.
 * This class implements the Spring RowMapper interface to customize the mapping process for User objects.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Component
public class UserRowMapper implements RowMapper<User> {

    /**
     * Maps a row of the ResultSet to a User object.
     *
     * @param rs     the ResultSet, pointing to the current row being mapped
     * @param rowNum the number of the current row
     * @return a User object with data fetched from the ResultSet
     * @throws SQLException if a database access error occurs or if other errors happen while processing the ResultSet
     */
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRole role = UserRole.fromString(rs.getString("role"));
        User user = new User(rs.getString("name"), rs.getString("password"), role);
        user.setId(rs.getLong("id"));
        return user;
    }
}