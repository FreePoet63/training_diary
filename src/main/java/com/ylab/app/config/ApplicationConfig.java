package com.ylab.app.config;

import com.ylab.app.web.security.JwtTokenFilter;
import com.ylab.app.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * ApplicationConfig class is responsible for configuring security beans using Spring Security.
 *
 * @author razlivinsky
 * @since 10.03.2024
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ApplicationConfig {
    private final JwtTokenProvider tokenProvider;
    private final ApplicationContext context;

    /**
     * Constructs an {@link MvcRequestMatcher.Builder} for creating customized MVC request matchers with an empty servlet path.
     *
     * @param introspector the {@link HandlerMappingIntrospector} for accurate URL matching with existing handler mappings.
     * @return a configured {@link MvcRequestMatcher.Builder} ready for customization.
     */
    @Bean
    public MvcRequestMatcher.Builder matcher(HandlerMappingIntrospector introspector){
        MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);
        builder.servletPath("");
        return builder;
    }

    /**
     * Password encoder bean for encoding passwords using BCryptPasswordEncoder.
     *
     * @return the password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean for managing authentication using the provided AuthenticationConfiguration.
     *
     * @param configuration the authentication configuration
     * @return the authentication manager bean
     * @throws Exception if an exception occurs during the configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Filter chain for configuring security rules and filters using HttpSecurity.
     *
     * @param httpSecurity the HttpSecurity object
     * @param builder      the builder
     * @return the configured security filter chain
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(
            final HttpSecurity httpSecurity, MvcRequestMatcher.Builder builder
    ) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.UNAUTHORIZED
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers(builder.pattern("/auth/**"))
                                .permitAll()
                                .requestMatchers(builder.pattern("/swagger-ui/**"))
                                .permitAll()
                                .requestMatchers(builder.pattern("/v3/**"))
                                .permitAll()
                                .anyRequest().authenticated())
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}