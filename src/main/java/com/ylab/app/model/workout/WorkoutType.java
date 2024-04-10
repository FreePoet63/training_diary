package com.ylab.app.model.workout;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * WorkoutType class represents the types of workouts available.
 *
 * This class includes fields for id and type of the workout.
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
public class WorkoutType {
    private Long id;
    private String type;

    /**
     * Creates and returns a new WorkoutType object with the specified type.
     *
     * @param type the type of workout
     * @return a new WorkoutType object with the given type
     */
    public static WorkoutType add(String type) {
        WorkoutType workoutType = new WorkoutType();
        workoutType.setType(type);
        return workoutType;
    }
}