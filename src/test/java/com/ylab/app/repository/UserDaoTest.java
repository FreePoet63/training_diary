package com.ylab.app.repository;

import com.ylab.app.dbService.dao.impl.UserDaoImpl;
import com.ylab.app.dbService.mappers.UserRowMapper;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * UserDaoTest class represents the test suite for the User DAO (Data Access Object).
 * It is responsible for verifying the data persistence layer, focusing on user-related operations.
 * The tests ensure that all CRUD (Create, Read, Update, Delete) operations perform correctly,
 * and that the DAO behaves as expected under various scenarios, including boundary cases and error conditions.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserDaoTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserRowMapper userRowMapper;

    @InjectMocks
    private UserDaoImpl userDao;

    private User user1;
    private User user2;
    private List<User> userList;

    @BeforeEach
    public void setUp() {
        user1 = new User("testUser", "testUser", UserRole.USER);
        user1.setId(1L);
        user2 = new User("testAdmin", "testAdmin", UserRole.ADMIN);
        user2.setId(2L);
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
    }

    @Test
    @DisplayName("insert user a new user into the database when successful")
    public void insertUser_InsertNewUser_WhenSuccessful() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        keyHolder.getKeyList().add(keys);

        ArgumentCaptor<PreparedStatementCreator> pscCaptor = ArgumentCaptor.forClass(PreparedStatementCreator.class);
        ArgumentCaptor<KeyHolder> keyHolderCaptor = ArgumentCaptor.forClass(KeyHolder.class);

        when(jdbcTemplate.update(pscCaptor.capture(), keyHolderCaptor.capture())).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1, KeyHolder.class);
            kh.getKeyList().addAll(keyHolder.getKeyList());
            return 1;
        });

        userDao.insertUser(user1);

        assertThat(user1.getId()).isEqualTo(1L);
        assertThat(keyHolderCaptor.getValue().getKey().longValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Get all users should return list of users from the database")
    void getAllUsers_ShouldReturnListOfUsers() {
        when(jdbcTemplate.query(anyString(), eq(userRowMapper))).thenReturn(userList);

        List<User> result = userDao.getAllUsers();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(userList);
    }

    @Test
    @DisplayName("Find user by id should return user or null")
    void findUserById_ShouldReturnUser() {
        when(jdbcTemplate.queryForObject(anyString(), eq(userRowMapper), eq(user1.getId()))).thenReturn(user1);

        User result = userDao.findUserById(user1.getId());

        assertThat(result).isEqualTo(user1);
    }

    @Test
    @DisplayName("Get user by login should return user or null")
    void getUserByLogin_ShouldReturnUser() {
        when(jdbcTemplate.queryForObject(anyString(), eq(userRowMapper), eq(user1.getName()))).thenReturn(user1);

        User result = userDao.getUserByLogin(user1.getName());

        assertThat(result).isEqualTo(user1);
    }

    @Test
    @DisplayName("getAllUsers throws DatabaseReadException when retrieval fails")
    public void getAllUsers_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.query(anyString(), eq(userRowMapper)))
                .thenThrow(new DataAccessException("Database access failure") {});

        Throwable thrown = catchThrowable(() -> userDao.getAllUsers());
        assertThat(thrown)
                .isInstanceOf(DatabaseReadException.class)
                .hasMessageContaining("Failed to retrieve all users");
    }

    @Test
    @DisplayName("findUserById throws UserValidationException when user with the given ID is not found")
    public void findUserById_ThrowUserValidationException_WhenUserNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), eq(userRowMapper), anyLong())).thenThrow(EmptyResultDataAccessException.class);

        User result = userDao.findUserById(9L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("findUserById throws DatabaseReadException when retrieval fails")
    public void findUserById_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.queryForObject(anyString(), eq(userRowMapper), anyLong()))
                .thenThrow(new DataAccessException("Database access failure") {});

        Throwable thrown = catchThrowable(() -> userDao.findUserById(1L));
        assertThat(thrown)
                .isInstanceOf(DatabaseReadException.class)
                .hasMessageContaining("Failed to retrieve user by id");
    }
}
