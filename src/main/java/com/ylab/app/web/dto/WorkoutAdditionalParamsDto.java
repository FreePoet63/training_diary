package com.ylab.app.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WorkoutAdditionalParamsDto class represents a Data Transfer Object for additional parameters of a workout.
 * This class includes the ID, parameters description, and corresponding value.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Workout Addition parameters DTO")
public class WorkoutAdditionalParamsDto {
    @Schema(description = "Workout Addition parameters Id", example = "1")
    private Long id;

    @Schema(description = "Workout parameter", example = "jumping")
    private String params;

    @Schema(description = "Total value", example = "50")
    private Long value;
}