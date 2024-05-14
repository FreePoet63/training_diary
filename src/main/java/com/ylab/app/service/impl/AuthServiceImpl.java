package com.ylab.app.service.impl;

import com.ylab.app.model.user.User;
import com.ylab.app.service.AuthService;
import com.ylab.app.service.UserService;
import com.ylab.app.web.dto.auth.JwtRequest;
import com.ylab.app.web.dto.auth.JwtResponse;
import com.ylab.app.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * AuthServiceImpl class provides implementation for authentication-related operations.
 *
 *author razlivinsky
 * @since 30.04.2024
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Logs in a user and generates a JWT response.
     *
     * @param loginRequest the login request containing user credentials
     * @return the JWT response with the generated token
     */
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userService.getUserByLogin(loginRequest.getUsername());

        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getName());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getName(), user.getRole()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getName()));
        return jwtResponse;
    }

    /**
     * Refreshes a JWT token using the provided refresh token.
     *
     * @param refreshToken the refresh token used to generate a new access token
     * @return the JWT response with the refreshed token
     */
    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}