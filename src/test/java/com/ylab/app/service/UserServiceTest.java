package com.ylab.app.service;

import com.ylab.app.dbService.dao.UserDao;
import com.ylab.app.exception.resourceException.ResourceNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register user with valid name, password and role")
    public void registerUserWithValidNamePasswordAndRole() throws SQLException {
        doNothing().when(dao).insertUser(any(User.class));

        User result = userService.registerUser("testUser", "testUser");

        assertThat(result.getName()).isEqualTo("testUser");
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

    @Test
    @DisplayName("Find of user by id")
    public void getFindUserById() throws SQLException {
        User user = new User(1L, "test", "123", UserRole.USER);

        when(dao.findUserById(user.getId())).thenReturn(user);

        User result = userService.getUserById(user.getId());

        assertThat(user.getName()).isEqualTo(result.getName());
    }

    @Test
    @DisplayName("Find of user by id is null")
    public void getFindUserByIdIsNull() throws SQLException {
        assertThatThrownBy(() -> userService.getUserById(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found.");
    }
}