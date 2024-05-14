package com.ylab.app.web.controller;

import com.ylab.app.model.user.User;
import com.ylab.app.service.AuthService;
import com.ylab.app.service.UserService;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.dto.auth.JwtRequest;
import com.ylab.app.web.dto.auth.JwtResponse;
import com.ylab.app.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController class handles authentication-related HTTP requests.
 *
 * @author razlivinsky
 * @since 11.03.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Login with JWT credentials and generate a JWT response.
     *
     * @param jwtRequest the JWT login request
     * @return the JWT response containing access and refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody JwtRequest jwtRequest) {
        JwtResponse response = authService.login(jwtRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Register a new user based on the provided UserDto.
     *
     * @param dto the UserDto containing user registration information
     * @return the UserDto of the registered user
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Validated @RequestBody UserDto dto) {
        User user = userMapper.userDtoToUser(dto);
        User createUser = userService.registerUser(user.getName(), user.getPassword());
        UserDto createdUserDto = userMapper.userToUserDto(createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    /**
     * Refresh the access token using the provided refresh token.
     *
     * @param refreshToken the refresh token to generate a new access token
     * @return the JWT response with the refreshed access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody String refreshToken) {
        JwtResponse response = authService.refresh(refreshToken);
        return ResponseEntity.ok(response);
    }
}