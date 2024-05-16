package com.ylab.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.app.config.ApplicationConfig;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.UserService;
import com.ylab.app.web.controller.UserController;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerIntegrationTest class for the UserController.
 * It focuses on testing the integration of the controller with other components,
 * such as database access, authentication, and request/response handling.
 * The class covers scenarios involving real dependencies and ensures proper
 * interaction between the controller and the rest of the application.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@Import(ApplicationConfig.class)
@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;
    private Long userId;
    private List<User> users;
    List<UserDto> userDtos;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        userId = 1L;
        user = new User(userId, "test", "test", UserRole.USER);
        userDto = new UserDto(userId, "test", "test", UserRole.USER);
        users = new ArrayList<>();
        users.add(user);
        userDtos = new ArrayList<>();
        userDtos.add(userDto);
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.listUserToUserDto(users)).thenReturn(userDtos);
        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);
    }

    @Test
    @DisplayName("Get user by ID should return user DTO")
    void getUserById_ShouldReturnUserDto() throws Exception {
       mockMvc.perform(get("/users/{id}", userId)
                       .with(SecurityMockMvcRequestPostProcessors.user("testUser").roles("USER")))
               .andExpect(status().isOk())
               .andExpect(content().json(mapper.writeValueAsString(userDto)));
    }

    @Test
    @DisplayName("Get user by ID should return user DTO")
    void getUserById_UserIsUnauthorized() throws Exception {
       mockMvc.perform(get("/users/{id}", userId))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get all users should return list of user DTOs")
    void getAllUsers_ShouldReturnListOfUserDtos() throws Exception {
        mockMvc.perform(get("/users/all")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtos)));
    }
}
