package com.ylab.app.web.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JwtResponse class represents the response object for JWT authentication, containing user identification details and tokens.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
}