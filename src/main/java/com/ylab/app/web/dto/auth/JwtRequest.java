package com.ylab.app.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JwtRequest class represents the request object for JWT authentication.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for login")
public class JwtRequest {
    @Schema(description = "name", example = "ylab")
    @NotNull(message = "Username must be not null.")
    private String username;

    @Schema(description = "password", example = "ylab")
    @NotNull(message = "Password must be not null.")
    private String password;
}