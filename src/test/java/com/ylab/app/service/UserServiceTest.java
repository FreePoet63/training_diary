package com.ylab.app.service;

import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * UserServiceTest class
 *
 * @author razlivinsky
 * @since 27.01.2024
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Register user with valid name, password and role")
    public void registerUserWithValidNamePasswordAndRole() {
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;

        userService.registerUser(name, password, role);

        assertThat(userService.getAllUsers())
                .hasSize(1)
                .first()
                .extracting(User::getName, User::getPassword, User::getRole)
                .containsExactly(name, password, role);
    }

    @Test
    @DisplayName("Register user with null name")
    public void registerUserWithNullName() {
        String name = null;
        String password = "123";
        UserRole role = UserRole.USER;

        assertThatThrownBy(() -> userService.registerUser(name, password, role))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Register user with existing name")
    public void registerUserWithExistingName() {
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;
        userService.registerUser(name, password, role);

        assertThatThrownBy(() -> userService.registerUser(name, password, role))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("User already exists");
    }

    @Test
    @DisplayName("Login user with valid name and password")
    public void loginUserWithValidNameAndPassword() {
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;
        userService.registerUser(name, password, role);

        User result = userService.loginUser(name, password);

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields()
                .isEqualTo(userService.getAllUsers().get(0));
    }

    @Test
    @DisplayName("Login user with null name")
    public void loginUserWithNullName() {
        String name = null;
        String password = "123";

        assertThatThrownBy(() -> userService.loginUser(name, password))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Login user with non-existing name")
    public void loginUserWithNonExistingName() {
        String name = "test";
        String password = "123";

        assertThatThrownBy(() -> userService.loginUser(name, password))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("User does not exist");
    }

    @Test
    @DisplayName("Login user with wrong password")
    public void loginUserWithWrongPassword() {
        String name = "test";
        String password = "123";
        UserRole role = UserRole.USER;
        userService.registerUser(name, password, role);

        assertThatThrownBy(() -> userService.loginUser(name, "456"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    @DisplayName("Check role for admin user")
    public void checkRoleForAdminUser() {
        String name = "admin";
        String password = "123";
        UserRole role = UserRole.ADMIN;
        userService.registerUser(name, password, role);
        when(user.getName()).thenReturn(name);
        when(user.getPassword()).thenReturn(password);
        when(user.getRole()).thenReturn(role);

        boolean result = userService.checkRole(user);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Check role for user user")
    public void checkRoleForUserUser() {
        String name = "user";
        String password = "123";
        UserRole role = UserRole.USER;
        userService.registerUser(name, password, role);
        when(user.getName()).thenReturn(name);
        when(user.getPassword()).thenReturn(password);
        when(user.getRole()).thenReturn(role);

        boolean result = userService.checkRole(user);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Check role for null user")
    public void checkRoleForNullUser() {
        User user = null;

        assertThatThrownBy(() -> userService.checkRole(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Invalid user");
    }

    @Test
    @DisplayName("Get all users")
    public void getAllUsers() {
        String name1 = "user1";
        String password1 = "123";
        UserRole role1 = UserRole.USER;
        userService.registerUser(name1, password1, role1);
        String name2 = "user2";
        String password2 = "456";
        UserRole role2 = UserRole.ADMIN;
        userService.registerUser(name2, password2, role2);

        List<User> result = userService.getAllUsers();

        assertThat(result).isNotNull()
                .hasSize(2)
                .extracting(User::getName, User::getPassword, User::getRole)
                .containsExactlyInAnyOrder(
                        tuple(name1, password1, role1),
                        tuple(name2, password2, role2)
                );
    }
}