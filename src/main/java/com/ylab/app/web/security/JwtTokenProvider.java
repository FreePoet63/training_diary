package com.ylab.app.web.security;

import com.ylab.app.exception.dbException.AccessDeniedException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.service.UserService;
import com.ylab.app.service.props.JwtProperties;
import com.ylab.app.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * JwtTokenProvider class handles the creation and validation of JWT tokens for authentication.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    /**
     * Initializes the JwtTokenProvider by setting the key using the specified secret from jwtProperties.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Creates an access token using the provided user information and roles.
     *
     * @param userId   the user id for the token
     * @param username the username for the token
     * @param role    the roles associated with the user
     * @return the generated access token
     */
    public String createAccessToken(Long userId, String username, UserRole role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    /**
     * Creates a refresh token using the provided user information.
     *
     * @param userId   the user id for the token
     * @param username the username for the token
     * @return the generated refresh token
     */
    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    /**
     * Refreshes the user tokens based on the provided refresh token.
     *
     * @param refreshToken the refresh token to validate and use for token refresh
     * @return the updated JWT response containing new access and refresh tokens
     */
    public JwtResponse refreshUserTokens(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if(!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getUserById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getName());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getName(), user.getRole()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getName()));
        return jwtResponse;
    }

    /**
     * Validates the provided token.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    /**
     * Retrieves the user ID from the provided token.
     *
     * @param token the token from which to retrieve the user ID
     * @return the user ID extracted from the token
     */
    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    /**
     * Retrieves the username from the provided token.
     *
     * @param token the token from which to retrieve the username
     * @return the username extracted from the token
     */
    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Retrieves the user authentication details based on the provided token.
     *
     * @param token the token from which to retrieve user authentication details
     * @return the user authentication details
     */
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}