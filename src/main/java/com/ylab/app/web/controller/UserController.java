package com.ylab.app.web.controller;

import com.ylab.app.model.user.User;
import com.ylab.app.service.UserService;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.mapper.UserMapper;
import com.ylab.aspect.EnableLogging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class responsible for handling user-related operations.
 * This class provides endpoints to retrieve user information.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@Tag(name = "User Controller", description = "User API")
@EnableLogging
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Retrieves a user entity by its unique ID.
     *
     * @param id the unique ID of the user to retrieve
     * @return a response entity containing the user DTO with the requested user information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get userDto by id")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.userToUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Retrieves a list of all users available in the system.
     *
     * @return a response entity containing a list of user DTOs representing all users
     */
    @GetMapping("/all")
    @Operation(summary = "Get all userDto")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = userMapper.listUserToUserDto(users);
        return ResponseEntity.ok(userDtos);
    }
}