package com.ylab.app.service;

import com.ylab.app.exception.resourceException.ResourceNotFoundException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.impl.AuthServiceImpl;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.web.dto.auth.JwtRequest;
import com.ylab.app.web.dto.auth.JwtResponse;
import com.ylab.app.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * AuthServiceTest class is designed to verify user authentication.
 * It utilizes Mockito to create mock objects necessary for testing.
 * Covers the main authentication scenarios, including successful system login,
 * handling of incorrect credentials, and exception processing.
 *
 * @author razlivinsky
 * @since 07.05.2024
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Successful User Login")
    void login() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        UserRole role = UserRole.USER;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setName(username);
        user.setRole(role);
        Mockito.when(userService.getUserByLogin(username))
                .thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, username, role))
                .thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username))
                .thenReturn(refreshToken);
        JwtResponse response = authService.login(request);
        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword())
                );
        Assertions.assertEquals(response.getUsername(), username);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    @DisplayName("Login failure on incorrect username")
    void loginWithIncorrectUsername() {
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setName(username);
        Mockito.when(userService.getUserByLogin(username))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    @DisplayName("Successful token refresh")
    void refresh() {
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(response);
        JwtResponse testResponse = authService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        Assertions.assertEquals(testResponse, response);
    }
}