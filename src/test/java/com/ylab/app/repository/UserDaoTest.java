package com.ylab.app.repository;

import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.dbService.mappers.UserRowMapper;
import com.ylab.app.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * UserDaoTest class represents the test suite for testing the functionality of the User DAO (Data Access Object) class.
 *
 * @author razlivinsky
 * @since 15.04.2024
 */
@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserRowMapper userRowMapper;

    @InjectMocks
    private UserDaoImpl userDao;

    @Test
    @DisplayName("Get all users should return list of users from the database")
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> userList = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(userList);

        List<User> result = userDao.getAllUsers();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(userList);
    }

    @Test
    @DisplayName("Find user by id should return user or null")
    void findUserById_ShouldReturnUserOrNull() {
        long userId = 1L;
        User user = new User();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(userId))).thenReturn(user);

        User result = userDao.findUserById(userId);

        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("Get user by login should return user or null")
    void getUserByLogin_ShouldReturnUserOrNull() {
        String login = "testUser";
        User user = new User();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(login))).thenReturn(user);

        User result = userDao.getUserByLogin(login);

        assertThat(result).isEqualTo(user);
    }
}
