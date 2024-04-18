package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.WorkoutDao;
import com.ylab.app.dbService.dao.impl.WorkoutDaoImpl;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.Audition;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WorkoutService;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * WorkoutServiceImpl class
 * <p>
 * Implements the WorkoutService interface managing workout operations such as adding,
 * editing, deleting workouts, as well as providing specific workout data retrieval and statistics.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class WorkoutServiceImpl implements WorkoutService {
    private UserService userService = new UserServiceImpl();
    private WorkoutDao workoutDao = new WorkoutDaoImpl();

    /**
     * Adds a workout to the system.
     *
     * @param workout the workout to add
     * @throws WorkoutException if adding the workout fails due to an underlying issue
     */
    @Override
    public void addWorkout(User user, Workout workout) {
        if (workout == null) {
            throw new WorkoutException("Incorrect workout!");
        }
        try {
            workout.setUser(user);
            workoutDao.insertWorkout(workout);
        } catch (SQLException e) {
            throw new WorkoutException("Error adding workout: " + e.getMessage());
        }
    }

    /**
     * Retrieves all workouts for a specific user on a given date.
     *
     * @param user       the user whose workouts are being queried
     * @param targetDate the date for which workouts are being queried
     * @return List of workouts matching the specified date and user criteria
     * @throws WorkoutException if there is an error retrieving the workouts
     */
    @Override
    public List<Workout> getWorkoutsOnDate(User user, LocalDateTime targetDate) {
        if (user == null) {
            throw new UserValidationException("Invalid user.");
        }
        if (targetDate == null) {
            throw new WorkoutException("Incorrect date!");
        }
        try {
            return workoutDao.findWorkoutsByUserAndDate(user, targetDate);
        } catch (SQLException e) {
            throw new WorkoutException("Error retrieving workouts: " + e.getMessage());
        }
    }

    /**
     * Edits an existing workout identified by a unique workoutId.
     *
     * @param updatedWorkout the updated workout details
     * @param workoutId      the identifier of the workout to be updated
     * @throws WorkoutException if the workout with the specified ID does not exist or cannot be edited
     */
    @Override
    public void editWorkout(User user, Workout updatedWorkout, Long workoutId) {
        try {
            updatedWorkout.setUser(user);
            workoutDao.editWorkout(updatedWorkout, workoutId);
        } catch (SQLException e) {
            throw new WorkoutException("Failed to update workout: " + e.getMessage());
        }
    }

    /**
     * Deletes a workout from the system based on its unique workoutId.
     *
     * @param workoutId the identifier of the workout to be deleted
     * @throws WorkoutException if the workout with the specified ID does not exist or cannot be deleted
     */
    @Override
    public void deleteWorkout(Long workoutId) {
        try {
            workoutDao.deleteWorkout(workoutId);
        } catch (SQLException e) {
            throw new WorkoutException("Failed to delete workout: " + e.getMessage());
        }
    }


    /**
     * Retrieves the total calories burned by a user within a specific time period.
     *
     * @param user      the user for whom calories are calculated
     * @param startDate the starting date of the period
     * @param endDate   the ending date of the period
     * @return the total calories burned during the specified period
     * @throws WorkoutException if there is an error in calculating the calories
     */
    @Override
    public int getCaloriesBurnedInTimePeriod(User user, LocalDateTime startDate, LocalDateTime endDate) {
        validationWorkoutUserAndDate(user, startDate, endDate);
        try {
            return workoutDao.getTotalCaloriesBurnedByUser(user, startDate, endDate);
        } catch (SQLException e) {
            throw new WorkoutException("Error in calculating burned calories for the period: " + e.getMessage());
        }
    }

    /**
     * Gets statistical data of additional parameters (e.g., heart rate, steps) for all workouts of a user within a given time frame.
     *
     * @param user      the user whose stats are being queried
     * @param type      the type of workout for which the stats are being queried
     * @param startDate the start date of the time period
     * @param endDate   the end date of the time period
     * @return a list containing the aggregated values of different additional parameters
     * @throws WorkoutException if there is an error in retrieving the additional parameters statistics
     */
    @Override
    public List<WorkoutAdditionalParams> getAdditionalParamsStats(User user, WorkoutType type, LocalDateTime startDate, LocalDateTime endDate) {
        validationWorkoutUserAndDate(user, startDate, endDate);
        try {
            return workoutDao.findWorkoutParamsByTypeUserAndDate(user, type, startDate, endDate);
        } catch (Exception e) {
            throw new WorkoutException("Error while obtaining statistics for additional parameters:: " + e.getMessage());
        }
    }

    /**
     * Retrieves all the workouts in the system for an admin user.
     *
     * @param adminUser the admin user requesting the workouts
     * @return a list of all workouts in the system
     * @throws IllegalArgumentException if the adminUser is null or unauthorized
     */
    @Override
    public List<Workout> getAllReadingsWorkouts(User adminUser) {
        if (adminUser == null || !userService.hasRoleAdmin(adminUser)) {
            throw new UserValidationException("Invalid or unauthorized user");
        }
        try {
            return workoutDao.findAllWorkoutList();
        } catch (SQLException e) {
            throw new WorkoutException("Not found list workouts " + e.getMessage());
        }
    }

    /**
     * Validates the user and date parameters for workout operations.
     *
     * This method ensures that the user object is not null and that both the start and end dates are provided.
     *
     * @param user The user object to validate.
     * @param startDate The start date of the period to validate.
     * @param endDate The end date of the period to validate.
     * @throws UserValidationException if the user object is null.
     * @throws WorkoutException if either the start or end date is null.
     */
    private void validationWorkoutUserAndDate(User user, Serializable startDate, Serializable endDate) {
        if(user == null) {
            throw new UserValidationException("incorrect user!");
        }
        if (startDate == null || endDate == null) {
            throw new WorkoutException("Incorrect date!");
        }
    }
}