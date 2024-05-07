package com.ylab.app.service;

import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.web.dto.WorkoutDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WorkoutService interface documentation.
 * <p>
 * This interface defines the operations for managing workouts including addition, modification, retrieval,
 * and deletion of workout sessions. It also provides functionalities to retrieve workout statistics such as
 * calories burned and additional parameters over specified time periods.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public interface WorkoutService {

    /**
     * Adds a workout to the system.
     *
     * @param user    the user
     * @param workout the workout instance to add
     * @return the workout
     */
    Workout addWorkout(User user, Workout workout);

    /**
     * Retrieves a list of workouts for a user on a specified date.
     *
     * @param user       the user whose workouts are to be retrieved
     * @param targetDate the date for which workouts are sought
     * @return List of workouts on the specified date
     */
    List<Workout> getWorkoutsOnDate(User user, LocalDateTime targetDate);

    /**
     * Edits an existing workout identified by workoutId with the details from updatedWorkout.
     *
     * @param user           the user
     * @param updatedWorkout the workout instance containing updated information
     * @param workoutId      the identifier of the workout to be updated
     * @return the workout
     */
    Workout editWorkout(User user, Workout updatedWorkout, Long workoutId);

    /**
     * Deletes a workout identified by a unique workoutId.
     *
     * @param workoutId the identifier of the workout to be deleted
     */
    void deleteWorkout(Long workoutId);

    /**
     * Calculates the total calories burned by a user within a specified time period.
     *
     * @param user      the user for whom calories burned is calculated
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @return the total calories burned during the period
     */
    int getCaloriesBurnedInTimePeriod(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Retrieves statistics for additional workout parameters (like heart rate, steps etc.) over a specified time period.
     *
     * @param user      the user for whom the statistics are to be retrieved
     * @param type      the type
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @return a list of additional parameter names and their aggregated values
     */
    List<WorkoutAdditionalParams> getAdditionalParamsStats(User user, WorkoutType type, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Retrieves a list of all recorded workout sessions in the system.
     *
     * @param adminUser The administrative user requesting the workout data. Must have administrative privileges.
     * @return A list of {@code Workout} objects representing all the workouts recorded in the system.
     */
    List<Workout> getAllReadingsWorkouts(User adminUser);

    /**
     * Retrieves a workout by its ID.
     *
     * @param workoutId the ID of the workout to retrieve
     * @return the workout corresponding to the given ID
     */
    Workout getWorkoutById(Long workoutId);
}
