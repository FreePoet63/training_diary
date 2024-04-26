package com.ylab.app.model.workout;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * WorkoutAdditionalParams class represents the additional parameters associated with a workout.
 *
 * This class includes fields for id, parameter name/description, and parameter value.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutAdditionalParams {
    private Long id;
    private String params;
    private Long value;
}