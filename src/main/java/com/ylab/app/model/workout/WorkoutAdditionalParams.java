package com.ylab.app.model.workout;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WorkoutAdditionalParams {
    private Long id;
    private String params;
    private Long value;

    /**
     * Creates and returns a new WorkoutAdditionalParams object with the specified parameter and value.
     *
     * @param param the name/description of the parameter
     * @param value the value of the parameter
     * @return a new WorkoutAdditionalParams object with the given parameter and value
     */
    public static WorkoutAdditionalParams add(String param, Long value) {
        WorkoutAdditionalParams workoutAdditionalParams = new WorkoutAdditionalParams();
        workoutAdditionalParams.setParams(param);
        workoutAdditionalParams.setValue(value);
        return workoutAdditionalParams;
    }
}