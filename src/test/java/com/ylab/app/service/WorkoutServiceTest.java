package com.ylab.app.service;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * WorkoutServiceTest class
 *
 * @author razlivinsky
 * @since 10.04.2024
 */
@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {
    private WorkoutService workoutService;

    private Workout workout;
    private User user;
    private LocalDateTime now;

    private Long workoutId = 1L;
    private int duration = 120;
    private int caloriesBurned = 350;
    private List<WorkoutType> workoutTypes = new ArrayList<>();
    private List<WorkoutAdditionalParams> listParams = new ArrayList<>();

    @BeforeEach
    void setUp() {
        workoutService = new WorkoutServiceImpl();
        user = new User(1L, "test", "test", UserRole.ADMIN);
        WorkoutType workoutType = new WorkoutType(1L, "strength");
        workoutTypes.add(workoutType);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(1L, "push-ups", 50L);
        now = LocalDateTime.now();
        listParams.add(params);
        workout = new Workout();
        workout.setId(workoutId);
        workout.setDate(now);
        workout.setDuration(duration);
        workout.setCaloriesBurned(caloriesBurned);
        workout.setWorkoutTypes(workoutTypes);
        workout.setParams(listParams);
        workout.setUser(user);
    }

    @Test
    @DisplayName("Add workout successfully")
    void addWorkoutSuccessfully() {
        workoutService.addWorkout(workout);
        List<Workout> workouts = workoutService.getWorkoutsOnDate(user, now);
        assertThat(workouts).contains(workout);
    }

    @Test
    @DisplayName("Throw exception when adding null workout")
    void throwExceptionWhenAddingNullWorkout() {
        assertThatThrownBy(() -> workoutService.addWorkout(null))
                .isInstanceOf(WorkoutException.class)
                .hasMessageContaining("Ошибка при добавлении тренировки");
    }

    @Test
    @DisplayName("Get workouts on specific date")
    void getWorkoutsOnSpecificDate() {
        workoutService.addWorkout(workout);
        List<Workout> workouts = workoutService.getWorkoutsOnDate(user, now);
        assertThat(workouts).hasSize(1);
        assertThat(workouts.get(0).getDate()).isEqualTo(now);
    }

    @Test
    @DisplayName("Get workouts on specific date is null")
    void getWorkoutsOnSpecificDateNullValue() {
        assertThatThrownBy(() -> workoutService.getWorkoutsOnDate(user, null))
                .isInstanceOf(WorkoutException.class)
                .hasMessageContaining("Incorrect date!");
    }

    @Test
    @DisplayName("Edit workout successfully")
    void editWorkoutSuccessfully() {
        workoutService.addWorkout(workout);
        Workout updatedWorkout = new Workout();
        updatedWorkout.setId(workout.getId());
        updatedWorkout.setDate(now.plusDays(1));
        updatedWorkout.setDuration(duration);
        updatedWorkout.setCaloriesBurned(caloriesBurned);
        updatedWorkout.setWorkoutTypes(workoutTypes);
        updatedWorkout.setParams(listParams);
        updatedWorkout.setUser(user);

        workoutService.editWorkout(updatedWorkout, workout.getId());
        List<Workout> workouts = workoutService.getWorkoutsOnDate(user, now.plusDays(1));
        assertThat(workouts).contains(updatedWorkout);
    }

    @Test
    @DisplayName("Delete workout successfully")
    void deleteWorkoutSuccessfully() {
        workoutService.addWorkout(workout);
        workoutService.deleteWorkout(workout.getId());
        List<Workout> workouts = workoutService.getWorkoutsOnDate(user, now);
        assertThat(workouts).doesNotContain(workout);
    }

    @Test
    @DisplayName("Calculate calories burned in time period")
    void calculateCaloriesBurnedInTimePeriod() {
        workoutService.addWorkout(workout);
        int calories = workoutService.getCaloriesBurnedInTimePeriod(user, now.minusDays(1), now.plusDays(1));
        assertThat(calories).isEqualTo(350);
    }

    @Test
    @DisplayName("Get all workouts for admin user")
    void getAllWorkoutsForAdminUser() {
        workoutService.addWorkout(workout);
        List<Workout> workouts = workoutService.getAllReadingsWorkouts(user);
        assertThat(workouts).contains(workout);
    }

    @Test
    @DisplayName("Get additional parameters statistics successfully")
    void getAdditionalParamsStatsSuccessfully() {
        workoutService.addWorkout(workout);
        Map<String, Long> stats = workoutService.getAdditionalParamsStats(user, now.minusDays(1), now.plusDays(1));
        assertThat(stats).containsEntry("push-ups", 50L);
    }

    @Test
    @DisplayName("Throw exception when getting additional parameters statistics with invalid user")
    void throwExceptionWhenGettingStatsWithInvalidUser() {
        assertThatThrownBy(() -> workoutService.getAdditionalParamsStats(user, null, now.plusDays(1)))
                .isInstanceOf(WorkoutException.class)
                .hasMessageContaining("Incorrect date!");
    }
}