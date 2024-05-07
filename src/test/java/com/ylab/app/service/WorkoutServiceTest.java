package com.ylab.app.service;

import com.ylab.app.dbService.dao.WorkoutDao;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * WorkoutServiceTest class represents the test suite for validating the functionality of the WorkoutService class.
 * It utilizes Mockito for mock-based unit testing.
 *
 * @author razlivinsky
 * @since 10.04.2024
 */
@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private WorkoutDao workoutDao;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private User user;
    private Workout workout;
    private LocalDateTime date;
    private Long workoutId;
    private WorkoutAdditionalParams params;
    private List<WorkoutAdditionalParams> listParams;

    @BeforeEach
    void setUp() {
        user = new User("test", "test", UserRole.USER);
        params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        listParams = new ArrayList<>();
        listParams.add(params);
        date = LocalDateTime.now();
        workout = new Workout();
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);
        workoutId = 1L;
    }

    @Test
    @DisplayName("Adding a workout should be successful")
    void addWorkout_ShouldAddWorkout() throws SQLException {
        doNothing().when(workoutDao).insertWorkout(any(Workout.class));

        workoutService.addWorkout(user, workout);

        verify(workoutDao).insertWorkout(workout);
    }

    @Test
    @DisplayName("Adding a workout should throw an exception if the workout is null")
    void addWorkout_ShouldThrowException_WhenWorkoutIsNull() {
        Workout nullWorkout = null;

        assertThatThrownBy(() -> workoutService.addWorkout(user, nullWorkout))
                .isInstanceOf(WorkoutException.class)
                .hasMessageContaining("Incorrect workout!");
    }

    @Test
    @DisplayName("Getting workouts on a date should return a list of workouts")
    void getWorkoutsOnDate_ShouldReturnWorkouts() throws SQLException {
        List<Workout> expectedWorkouts = Collections.singletonList(workout);
        when(workoutDao.findWorkoutsByUserAndDate(user, date)).thenReturn(expectedWorkouts);

        List<Workout> actualWorkouts = workoutService.getWorkoutsOnDate(user, date);

        assertThat(actualWorkouts).isEqualTo(expectedWorkouts);
    }

    @Test
    @DisplayName("Editing a workout should be successful")
    void editWorkout_ShouldEditWorkout() throws SQLException {
        when(workoutDao.findWorkoutById(workoutId)).thenReturn(workout);
        Workout updatedWorkout = new Workout();
        Workout result = workoutService.editWorkout(user, updatedWorkout, workoutId);

        verify(workoutDao).editWorkout(updatedWorkout, workoutId);

        assertEquals(updatedWorkout, result);
    }

    @Test
    @DisplayName("Deleting a workout should be successful")
    void deleteWorkout_ShouldDeleteWorkout() throws SQLException {
        when(workoutDao.findWorkoutById(workoutId)).thenReturn(workout);

        workoutService.deleteWorkout(workoutId);

        verify(workoutDao).deleteWorkout(workoutId);
    }

    @Test
    @DisplayName("Calculating calories burned over a time period should be accurate")
    void getCaloriesBurnedInTimePeriod_ShouldCalculateCalories() throws SQLException {
        int expectedCalories = 500;
        when(workoutDao.getTotalCaloriesBurnedByUser(user, date, date)).thenReturn(expectedCalories);

        int actualCalories = workoutService.getCaloriesBurnedInTimePeriod(user, date, date);

        assertThat(actualCalories).isEqualTo(expectedCalories);
    }

    @Test
    @DisplayName("Getting statistics for additional parameters should return statistics")
    void getAdditionalParamsStats_ShouldReturnStats() throws SQLException {
        List<WorkoutAdditionalParams> expectedParams = Collections.emptyList();
        when(workoutDao.findWorkoutParamsByTypeUserAndDate(user, WorkoutType.CARDIO, date, date)).thenReturn(expectedParams);

        List<WorkoutAdditionalParams> actualParams = workoutService.getAdditionalParamsStats(user, WorkoutType.CARDIO, date, date);

        assertThat(actualParams).isEqualTo(expectedParams);
    }

    @Test
    @DisplayName("Getting all workouts should return a list of all workouts")
    void getAllReadingsWorkouts_ShouldReturnAllWorkouts() throws SQLException {
        List<Workout> expectedWorkouts = Collections.singletonList(workout);
        when(userService.hasRoleAdmin(user)).thenReturn(true);
        when(workoutDao.findAllWorkoutList()).thenReturn(expectedWorkouts);

        List<Workout> actualWorkouts = workoutService.getAllReadingsWorkouts(user);

        assertThat(actualWorkouts).isEqualTo(expectedWorkouts);
    }

    @Test
    @DisplayName("Getting all workouts should throw an exception if the user is not an administrator")
    void getAllReadingsWorkouts_ShouldThrowException_WhenUserIsNotAdmin() {
        when(userService.hasRoleAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> workoutService.getAllReadingsWorkouts(user))
                .isInstanceOf(UserValidationException.class)
                .hasMessageContaining("Invalid or unauthorized user");
    }
}