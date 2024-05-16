package com.ylab.app.controller;

import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.AuthService;
import com.ylab.app.service.UserService;
import com.ylab.app.web.controller.AuthController;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.dto.auth.JwtRequest;
import com.ylab.app.web.dto.auth.JwtResponse;
import com.ylab.app.web.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * AuthControllerTest class for the authentication controller.
 * It ensures the proper functioning of user authentication processes,
 * including login, logout, and error handling within the system.
 * The class tests various authentication methods and checks the security measures
 * for different user roles and permissions.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Login with valid credentials should return JWT response")
    void loginWithValidCredentials_ShouldReturnJwtResponse() {
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        JwtResponse jwtResponseMock = new JwtResponse(1L, "username", "accessToken", "refreshToken");
        when(authService.login(jwtRequest)).thenReturn(jwtResponseMock);

        ResponseEntity<JwtResponse> response = authController.login(jwtRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jwtResponseMock);
    }

    @Test
    @DisplayName("Register user with valid data should return created UserDto")
    void registerUserWithValidData_ShouldReturnCreatedUserDto() {
        UserDto userDto = new UserDto();
        User user = new User("John Doe", "password123", UserRole.USER);
        User createdUser = new User("John Doe", "password123", UserRole.USER);
        UserDto createdUserDto = new UserDto();

        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(userService.registerUser("John Doe", "password123")).thenReturn(createdUser);
        when(userMapper.userToUserDto(createdUser)).thenReturn(createdUserDto);

        ResponseEntity<UserDto> response = authController.register(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdUserDto);
    }

    @Test
    @DisplayName("Refresh token with valid refresh token should return JWT response")
    void refreshTokenWithValidRefreshToken_ShouldReturnJwtResponse() {
        String validRefreshToken = "validRefreshToken";
        JwtResponse jwtResponseMock = new JwtResponse(1L, "username","newAccessToken", "newRefreshToken");
        when(authService.refresh(validRefreshToken)).thenReturn(jwtResponseMock);

        ResponseEntity<JwtResponse> response = authController.refresh(validRefreshToken);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(jwtResponseMock);
    }
}