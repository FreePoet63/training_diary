package com.ylab.app.service.impl;

import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WorkoutService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private List<Workout> workouts = new ArrayList<>();
    private UserService userService = new UserServiceImpl();

    /**
     * Adds a workout to the system.
     *
     * @param workout the workout to add
     * @throws WorkoutException if adding the workout fails due to an underlying issue
     */
    public void addWorkout(Workout workout) {
        if (workout == null) {
            throw new WorkoutException("Incorrect workout!");
        }
        try {
            workouts.add(workout);
        } catch (Exception e) {
            throw new WorkoutException("Error adding a workout: " + e.getMessage());
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
    public List<Workout> getWorkoutsOnDate(User user, LocalDateTime targetDate) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (targetDate == null) {
            throw new WorkoutException("Incorrect date!");
        }
        try {
            return workouts.stream()
                    .filter(workout -> workout.getUser().equals(user))
                    .filter(workout -> workout.getDate().toLocalDate().equals(targetDate.toLocalDate()))
                    .sorted(Comparator.comparing(Workout::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new WorkoutException("Error while receiving training by date: " + e.getMessage());
        }
    }

    /**
     * Edits an existing workout identified by a unique workoutId.
     *
     * @param updatedWorkout the updated workout details
     * @param workoutId      the identifier of the workout to be updated
     * @throws WorkoutException if the workout with the specified ID does not exist or cannot be edited
     */
    public void editWorkout(Workout updatedWorkout, Long workoutId) {
        Workout workoutToEdit = findWorkoutById(workoutId);
        if (workoutToEdit != null) {
            int index = workouts.indexOf(workoutToEdit);
            if (index != -1) {
                workouts.set(index, updatedWorkout);
                System.out.println("Training successfully edition.");
            } else {
                throw new WorkoutException("Workout with such ID not found.");
            }
        } else {
            throw new WorkoutException("Workout with specified ID not found.");
        }
    }

    /**
     * Deletes a workout from the system based on its unique workoutId.
     *
     * @param workoutId the identifier of the workout to be deleted
     * @throws WorkoutException if the workout with the specified ID does not exist or cannot be deleted
     */
    public void deleteWorkout(Long workoutId) {
        Workout workoutToDelete = findWorkoutById(workoutId);
        if (workoutToDelete != null) {
            workouts.remove(workoutToDelete);
            System.out.println("Training successfully removed.");
        } else {
            throw new WorkoutException("Workout with this ID not found or belongs to user.");
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
    public int getCaloriesBurnedInTimePeriod(User user, LocalDateTime startDate, LocalDateTime endDate) {
        validationWorkoutUserAndDate(user, startDate, endDate);
        try {
            return workouts.stream()
                    .filter(workout -> workout.getUser().equals(user))
                    .filter(workout -> workout.getDate().isAfter(startDate) && workout.getDate().isBefore(endDate))
                    .mapToInt(Workout::getCaloriesBurned)
                    .sum();
        } catch (Exception e) {
            throw new WorkoutException("Error in calculating burned calories for the period: " + e.getMessage());
        }
    }

    /**
     * Gets statistical data of additional parameters (e.g., heart rate, steps) for all workouts of a user within a given time frame.
     *
     * @param user      the user whose stats are being queried
     * @param startDate the start date of the time period
     * @param endDate   the end date of the time period
     * @return a map containing the aggregated values of different additional parameters
     * @throws WorkoutException if there is an error in retrieving the additional parameters statistics
     */
    public Map<String, Long> getAdditionalParamsStats(User user, LocalDateTime startDate, LocalDateTime endDate) {
        validationWorkoutUserAndDate(user, startDate, endDate);
        try {
            return workouts.stream()
                    .filter(workout -> workout.getUser().equals(user))
                    .filter(workout -> workout.getDate().isAfter(startDate) && workout.getDate().isBefore(endDate))
                    .flatMap(workout -> workout.getParams().stream())
                    .collect(Collectors.groupingBy(
                            WorkoutAdditionalParams::getParams,
                            Collectors.summingLong(WorkoutAdditionalParams::getValue)
                    ));
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
    public List<Workout> getAllReadingsWorkouts(User adminUser) {
        if (adminUser == null || !userService.checkRole(adminUser)) {
            throw new UserValidationException("Invalid or unauthorized user");
        }
        return workouts;
    }

    /**
     * Finds a workout in the system by its unique ID.
     *
     * @param workoutId the ID of the workout to search for
     * @return the workout found by the ID, or null if not found
     */
    private Workout findWorkoutById(Long workoutId) {
        return workouts.stream()
                .filter(w -> w.getId().equals(workoutId))
                .findFirst()
                .orElse(null);
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