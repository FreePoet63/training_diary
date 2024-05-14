package com.ylab.app.web.security;

import com.ylab.app.model.user.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

/**
 * JwtEntityFactory class contains a method for creating JwtEntity objects based on User information.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
public class JwtEntityFactory {

    /**
     * Create a JwtEntity object based on the provided User object.
     *
     * @param user the User object from which to create the JwtEntity
     * @return the JwtEntity containing the user's information
     */
    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}