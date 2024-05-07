package com.ylab.app.dbService.dao;

import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * WorkoutDao interface provides methods for interacting with workout data in the database.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public interface WorkoutDao {

    /**
     * Inserts a new workout into the database.
     *
     * @param workout the workout to insert
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    void insertWorkout(Workout workout) throws SQLException;

    /**
     * Retrieves a list of workouts for a specific user on a given date.
     *
     * @param user       the user for whom workouts are being queried
     * @param targetDate the target date for the workouts
     * @return the list of workouts matching the specified user and date criteria
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    List<Workout> findWorkoutsByUserAndDate(User user, LocalDateTime targetDate) throws SQLException;

    /**
     * Updates an existing workout identified by its ID.
     *
     * @param updatedWorkout the updated workout details
     * @param workoutId      the ID of the workout to update
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    void editWorkout(Workout updatedWorkout, Long workoutId) throws SQLException;

    /**
     * Deletes a workout from the database based on its ID.
     *
     * @param workoutId the ID of the workout to delete
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    void deleteWorkout(Long workoutId) throws SQLException;

    /**
     * Retrieves the total calories burned by a user within a specified time period.
     *
     * @param user      the user for whom calories burned are being queried
     * @param startDate the start date of the time period
     * @param endDate   the end date of the time period
     * @return the total calories burned by the user in the specified time period
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    int getTotalCaloriesBurnedByUser(User user, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

    /**
     * Retrieves a list of workout additional parameters for a specific user, workout type, and date range.
     *
     * @param user        the user for whom the additional parameters are being queried
     * @param workoutType the type of workout for which the parameters are being queried
     * @param startDate   the start date of the time range
     * @param endDate     the end date of the time range
     * @return the list of workout additional parameters matching the specified criteria
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    List<WorkoutAdditionalParams> findWorkoutParamsByTypeUserAndDate(User user, WorkoutType workoutType, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

    /**
     * Retrieves a list of all workouts from the database.
     *
     * @return the list of all workouts in the database
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    List<Workout> findAllWorkoutList() throws SQLException;

    /**
     * Find a workout by its ID.
     *
     * @param workoutId the ID of the workout to find
     * @return the workout corresponding to the given ID
     * @throws SQLException if a database access error occurs
     */
    Workout findWorkoutById(Long workoutId) throws SQLException;
}