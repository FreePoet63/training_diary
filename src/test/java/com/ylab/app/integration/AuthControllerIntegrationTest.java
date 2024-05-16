package com.ylab.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.app.config.ApplicationConfig;
import com.ylab.app.service.AuthService;
import com.ylab.app.service.UserService;
import com.ylab.app.web.controller.AuthController;
import com.ylab.app.web.dto.auth.JwtRequest;
import com.ylab.app.web.dto.auth.JwtResponse;
import com.ylab.app.web.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * AuthControllerIntegrationTest class for the AuthController.
 * It assesses the controller's interactions with other application components,
 * ensuring seamless operation across authentication mechanisms, user data handling,
 * and endpoint security. The tests validate the cohesive performance of the
 * authentication processes within the full application context.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@Import(ApplicationConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private JwtRequest jwtRequest;
    private JwtResponse jwtResponseMock;
    private JwtResponse jwtResponseMockRefresh;
    private ObjectMapper mapper = new ObjectMapper();
    String validRefreshToken = "validRefreshToken";

    @BeforeEach
    public void setUp() {
        jwtRequest = new JwtRequest("username", "password");
        jwtResponseMock = new JwtResponse(1L, "username", "accessToken", "refreshToken");
        jwtResponseMockRefresh = new JwtResponse(1L, "username", "newAccessToken", "newRefreshToken");
        when(authService.refresh(anyString())).thenReturn(jwtResponseMock);
        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponseMock);
    }

    @Test
    @DisplayName("Login with valid credentials should return JWT response")
    void loginWithValidCredentials_ShouldReturnJwtResponse() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(jwtResponseMock.getUsername()))
                .andExpect(jsonPath("$.accessToken").value(jwtResponseMock.getAccessToken()));
    }

    @Test
    @DisplayName("Refresh token with valid refresh token should return JWT response")
    void refreshTokenWithValidRefreshToken_ShouldReturnJwtResponse() throws Exception {
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validRefreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(jwtResponseMock.getUsername()))
                .andExpect(jsonPath("$.accessToken").value(jwtResponseMock.getAccessToken()));
    }
}


