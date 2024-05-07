package com.ylab.app.web.dto;

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
public class WorkoutAdditionalParamsDto {
    private Long id;
    private String params;
    private Long value;
}