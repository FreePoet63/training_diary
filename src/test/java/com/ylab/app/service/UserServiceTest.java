package com.ylab.app.service;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * UserServiceTest class represents the test suite for validating the functionality of the UserService class.
 * It utilizes Mockito for mock-based unit testing with lenient strictness settings.
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @Mock
    private UserDao dao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register user with valid name, password and role")
    public void registerUserWithValidNamePasswordAndRole() throws SQLException {
        doNothing().when(dao).insertUser(any(User.class));

        User result = userService.registerUser("testUser", "password");

        assertThat(result.getName()).isEqualTo("testUser");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("Register user with null name")
    public void registerUserWithNullName() {
        String name = null;
        String password = "123";

        assertThatThrownBy(() -> userService.registerUser(name, password))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");

        verifyNoInteractions(dao);
    }

    @Test
    @DisplayName("Login user with valid name and password")
    public void loginUserWithValidNameAndPassword() throws SQLException {
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;
        User expected = new User(name, password, role);

        doNothing().when(dao).insertUser(any(User.class));
        when(dao.findUserByNameAndPassword(name, password)).thenReturn(expected);

        User user = userService.registerUser(name, password);
        User result = userService.loginUser(name, password);

        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("Login user with non-existing name")
    public void loginUserWithNonExistingName() {
        String name = "usr";
        String password = "123";

        assertThatThrownBy(() -> userService.loginUser(name, password))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Login user with invalid name or password")
    public void loginUserWithInvalidNameOrPassword() throws SQLException {
        String name = "test";
        String password = "123";
        String wrongName = "wrong";
        String wrongPassword = "wrong";

        when(dao.findUserByNameAndPassword(anyString(), anyString())).thenReturn(null);

        assertThrows(UserValidationException.class, () -> userService.loginUser(wrongName, password));
        assertThrows(UserValidationException.class, () -> userService.loginUser(name, wrongPassword));
    }

    @Test
    @DisplayName("Check role for admin user")
    public void checkRoleForAdminUser() throws SQLException {
        String name = "admin";
        String password = "123";
        UserRole role = UserRole.ADMIN;
        User expected = new User(name, password, role);

        when(dao.findUserByNameAndPassword(name, password)).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        boolean result = userService.hasRoleAdmin(expected);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check role for user user")
    public void checkRoleForUserUser() throws SQLException {
        String name = "user";
        String password = "123";
        UserRole role = UserRole.USER;
        User expected = new User(name, password, role);

        when(dao.findUserByNameAndPassword(name, password)).thenReturn(expected);
        when(dao.getAllUsers()).thenReturn(Collections.singletonList(expected));

        boolean result = userService.hasRoleAdmin(expected);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Check role for null user")
    public void checkRoleForNullUser() {
        User user = null;

        assertThatThrownBy(() -> userService.hasRoleAdmin(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Get all users")
    public void getAllUsers() throws SQLException {
        User user1 = new User("user1", "123", UserRole.USER);
        User user2 = new User("user2", "456", UserRole.ADMIN);

        List<User> result = List.of(user1, user2);

        when(dao.getAllUsers()).thenReturn(result);

        assertThat(result).filteredOn(user -> user.getRole().equals(UserRole.USER)).isNotEmpty();
        assertThat(result).filteredOn(user -> user.getRole().equals(UserRole.ADMIN)).isNotEmpty();
    }

    @Test
    @DisplayName("Check role for admin user")
    public void getFindUserByLogin() throws SQLException {
        User user = new User("test", "123", UserRole.USER);

        when(dao.getUserByLogin(user.getName())).thenReturn(user);

        User result = userService.getUserByLogin(user.getName());

        assertThat(user.getName()).isEqualTo(result.getName());
    }
}