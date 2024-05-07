package com.ylab.app.web.security;

import com.ylab.app.model.user.User;
import com.ylab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JwtUserDetailsService class provides the user details for JWT authentication.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    /**
     * Load user details by username for JWT authentication.
     *
     * @param username the username of the user
     * @return the UserDetails containing user information
     * @throws UsernameNotFoundException if the specified username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByLogin(username);
        return JwtEntityFactory.create(user);
    }
}