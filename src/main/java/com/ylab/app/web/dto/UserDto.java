package com.ylab.app.web.dto;

import com.ylab.app.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDto class represents a Data Transfer Object for user-related information.
 * This class includes the user's ID, name, password, and role.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private UserRole role;
}