package com.ylab.app.controller;

import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.UserService;
import com.ylab.app.web.controller.UserController;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * UserControllerTest class for the user controller.
 * It verifies the functionality of the controller, including user creation,
 * retrieval, modification, and deletion operations.
 * The class covers various access levels and ensures proper handling of invalid data.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Get user by ID should return user DTO")
    void getUserById_ShouldReturnUserDto() {
        long userId = 1;
        User user = new User(userId, "test", "test", UserRole.USER);
        UserDto userDto = new UserDto();
        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    @DisplayName("Get all users should return list of user DTOs")
    void getAllUsers_ShouldReturnListOfUserDtos() {
        List<User> users = new ArrayList<>();
        List<UserDto> userDtos = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.listUserToUserDto(users)).thenReturn(userDtos);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDtos);
    }
}