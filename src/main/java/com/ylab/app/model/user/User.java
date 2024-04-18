package com.ylab.app.model.user;

import lombok.*;

/**
 * Represents a user in the system.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private UserRole role;

    /**
     * Instantiates a new User with the specified name, password, and role.
     *
     * @param name     the name of the user
     * @param password the password for the user
     * @param role     the role of the user
     */
    public User(String name, String password, UserRole role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
}