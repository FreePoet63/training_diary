package com.ylab.app.service.impl;

import com.ylab.app.dbService.dao.WorkoutDao;
import com.ylab.app.exception.resourceException.ResourceNotFoundException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    private final UserService userService;
    private final WorkoutDao workoutDao;

    /**
     * Adds a new workout to the system.
     *
     * @param user    the user associated with the workout
     * @param workout the workout to be added
     * @return the added workout
     * @throws WorkoutException if adding the workout fails due to an underlying issue
     */
    @Override
    public Workout addWorkout(User user, Workout workout) {
        if (workout == null) {
            throw new WorkoutException("Incorrect workout!");
        }
        workout.setUser(user);
        workoutDao.insertWorkout(workout);
        return workout;
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
        if (targetDate == null) {
            throw new WorkoutException("Incorrect date!");
        }
        return workoutDao.findWorkoutsByUserAndDate(user, targetDate);
    }

    /**
     * Edits an existing workout identified by a unique workoutId.
     *
     * @param user           the user editing the workout
     * @param updatedWorkout the updated workout details
     * @param workoutId      the identifier of the workout to be updated
     * @return the edited workout
     * @throws WorkoutException if the workout with the specified ID does not exist or cannot be edited
     */
    @Override
    public Workout editWorkout(User user, Workout updatedWorkout, Long workoutId) {
        Workout existingWorkout = workoutDao.findWorkoutById(workoutId);
        if (existingWorkout == null) {
            throw new ResourceNotFoundException("Resource not found");
        }
        updatedWorkout.setUser(user);
        workoutDao.editWorkout(updatedWorkout, workoutId);
        return updatedWorkout;
    }

    /**
     * Deletes a workout from the system based on its unique workoutId.
     *
     * @param workoutId the identifier of the workout to be deleted
     * @throws WorkoutException if the workout with the specified ID does not exist or cannot be deleted
     */
    @Override
    public void deleteWorkout(Long workoutId) {
        Workout existingWorkout = workoutDao.findWorkoutById(workoutId);
        if (existingWorkout == null) {
            throw new ResourceNotFoundException("Resource not found");
        }
        workoutDao.deleteWorkout(workoutId);

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
        validationWorkoutUserAndDate(startDate, endDate);
        return workoutDao.getTotalCaloriesBurnedByUser(user, startDate, endDate);
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
        validationWorkoutUserAndDate(startDate, endDate);
        return workoutDao.findWorkoutParamsByTypeUserAndDate(user, type, startDate, endDate);
    }

    /**
     * Retrieves all the workouts in the system for an admin user.
     *
     * @param adminUser the admin user requesting the workouts
     * @return a list of all workouts in the system
     * @throws UserValidationException if the adminUser is null or unauthorized
     */
    @Override
    public List<Workout> getAllReadingsWorkouts(User adminUser) {
        if (adminUser == null || !userService.hasRoleAdmin(adminUser)) {
            throw new UserValidationException("Invalid or unauthorized user");
        }
        return workoutDao.findAllWorkoutList();
    }

    /**
     * Retrieves a workout by its ID.
     *
     * @param workoutId the ID of the workout to retrieve
     * @return the workout corresponding to the given ID
     * @throws ResourceNotFoundException if the workout with the specified ID is not found
     */
    @Override
    public Workout getWorkoutById(Long workoutId) {
        Workout existingWorkout = workoutDao.findWorkoutById(workoutId);
        if (existingWorkout == null) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return existingWorkout;
    }

    /**
     * Validates the user and date parameters for workout operations.
     *
     * This method ensures that the user object is not null and that both the start and end dates are provided.
     *
     * @param startDate The start date of the period to validate.
     * @param endDate The end date of the period to validate.
     * @throws WorkoutException if either the start or end date is null.
     */
    private void validationWorkoutUserAndDate(Serializable startDate, Serializable endDate) {
        if (startDate == null || endDate == null) {
            throw new WorkoutException("Incorrect date!");
        }
    }
}