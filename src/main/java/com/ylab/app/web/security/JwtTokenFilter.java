package com.ylab.app.web.security;

import com.ylab.app.exception.userException.UserValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * JwtTokenFilter class represents a filter for processing JWT tokens during authentication.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider tokenProvider;

    /**
     * Performs JWT token validation and sets the authentication context for the request.
     *
     * @param servletRequest  the request to process
     * @param servletResponse the response to process
     * @param filterChain     the filter chain for invoking the next filter
     * @throws IOException      if an I/O error occurs during the filter execution
     * @throws ServletException if a servlet-specific error occurs during the filter execution
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken != null && tokenProvider.validateToken(bearerToken)) {
            try {
                Authentication authentication = tokenProvider.getAuthentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (UserValidationException ignore) {}
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}