package com.ylab.app.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ylab.app.model.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
@Schema(description = "User DTO")
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(description = "User name", example = "Y_lab")
    @NotNull(message = "Name must be not null.")
    @Length(max = 255, message = "Name length must be smaller than 255 symbols.")
    private String name;

    @Schema(description = "User password", example = "Y_lab")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null.")
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserRole role;
}