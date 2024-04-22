package com.ylab.app.model.user;

import com.ylab.app.exception.userException.UserValidationException;

/**
 * The UserRole enum represents the various roles that users can have within the system.
 *
 * @author razlivinsky
 * @since 01.02.2024
 */
public enum UserRole {
    ADMIN,
    USER;

    /**
     * Returns the UserRole enum value from the given role string.
     *
     * @param roleStr the role string
     * @return the corresponding UserRole enum value
     * @throws UserValidationException if the role string does not match any UserRole
     */
    public static UserRole fromString(String roleStr) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new UserValidationException("Unknown role: " + roleStr);
    }
}