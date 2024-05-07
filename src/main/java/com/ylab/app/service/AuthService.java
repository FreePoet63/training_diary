package com.ylab.app.service;

import com.ylab.app.web.dto.auth.JwtRequest;
import com.ylab.app.web.dto.auth.JwtResponse;

/**
 * AuthService interface defines methods for user authentication.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
public interface AuthService {

    /**
     * Logs in a user and generates a JWT response.
     *
     * @param loginRequest the login request containing user credentials
     * @return the JWT response with the generated token
     */
    JwtResponse login(JwtRequest loginRequest);

    /**
     * Refreshes a JWT token using the provided refresh token.
     *
     * @param refreshToken the refresh token used to generate a new access token
     * @return the JWT response with the refreshed token
     */
    JwtResponse refresh(String refreshToken);
}