package com.ylab.app.web.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * JwtEntity class represents the entity for JWT-based user details.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Data
@AllArgsConstructor
public class JwtEntity implements UserDetails {
    private Long id;
    private final String username;
    private final String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Checks if the user account is not expired.
     *
     * @return true if the user's account is not expired, otherwise false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the user account is not locked.
     *
     * @return true if the user's account is not locked, otherwise false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the user's credentials are not expired.
     *
     * @return true if the user's credentials are not expired, otherwise false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the user is enabled.
     *
     * @return true if the user is enabled, otherwise false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}