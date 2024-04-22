package com.ylab.app.model.workout;

import com.ylab.app.exception.workoutException.WorkoutException;

/**
 * WorkoutType class represents the types of workouts available.
 * <p>
 * This class includes fields for id and type of the workout.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public enum WorkoutType {
    CARDIO,
    STRENGTH_TRAINING,
    HIGH_INTENSITY_INTERVAL_TRAINING,
    FLEXIBILITY,
    BALANCE,
    ENDURANCE,
    CROSSFIT,
    YOGA,
    PILATES,
    AEROBICS,
    SPINNING,
    DANCE;

    /**
     * Converts a string to the corresponding WorkoutType enum value.
     *
     * @param workoutStr the string representation of the workout type
     * @return the WorkoutType enum value corresponding to the input string
     * @throws WorkoutException if the input string does not match any known workout type
     */
    public static WorkoutType fromString(String workoutStr) {
        for (WorkoutType type : WorkoutType.values()) {
            if (type.name().equalsIgnoreCase(workoutStr)) {
                return type;
            }
        }
        throw new WorkoutException("Unknown role: " + workoutStr);
    }
}