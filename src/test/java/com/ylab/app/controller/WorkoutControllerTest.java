package com.ylab.app.controller;

import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WorkoutService;
import com.ylab.app.web.controller.WorkoutController;
import com.ylab.app.web.dto.WorkoutAdditionalParamsDto;
import com.ylab.app.web.dto.WorkoutDto;
import com.ylab.app.web.mapper.WorkoutAdditionalParamsMapper;
import com.ylab.app.web.mapper.WorkoutMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * WorkoutControllerTest class
 *
 * @author razlivinsky
 * @since 07.05.2024
 */
@ExtendWith(MockitoExtension.class)
public class WorkoutControllerTest {
    @Mock
    private UserDetails userDetails;

    @Mock
    private UserService userService;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutAdditionalParamsMapper paramsMapper;

    @Mock
    private WorkoutMapper workoutMapper;

    @InjectMocks
    private WorkoutController workoutController;

    @Test
    @DisplayName("Add a new workout should return added workout details")
    void addWorkout_ShouldReturnAddedWorkoutDetails() {
        User user = new User();
        WorkoutDto workoutDto = new WorkoutDto();
        Workout addedWorkout = new Workout();
        when(userService.getUserByLogin(userDetails.getUsername())).thenReturn(user);
        when(workoutMapper.workoutDtoToWorkout(workoutDto)).thenReturn(addedWorkout);

        ResponseEntity<WorkoutDto> response = workoutController.addWorkout(userDetails, workoutDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Get all workouts for a specific user on a given date should return list of workouts")
    void getWorkoutsOnDate_ShouldReturnListOfWorkouts() {
        User user = new User();
        String targetDate = "2024-05-07T12:00:00";
        List<Workout> workoutList = new ArrayList<>();
        when(userService.getUserByLogin(userDetails.getUsername())).thenReturn(user);
        when(workoutService.getWorkoutsOnDate(user, LocalDateTime.parse(targetDate))).thenReturn(workoutList);

        ResponseEntity<List<WorkoutDto>> response = workoutController.getWorkoutsOnDate(userDetails, targetDate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Edit an existing workout should return updated workout details")
    void editWorkout_ShouldReturnUpdatedWorkoutDetails() {
        User user = new User();
        Long workoutId = 1L;
        WorkoutDto workoutDto = new WorkoutDto();
        Workout updatedWorkout = new Workout();
        when(userService.getUserByLogin(userDetails.getUsername())).thenReturn(user);
        when(workoutMapper.workoutDtoToWorkout(workoutDto)).thenReturn(updatedWorkout);
        when(workoutService.editWorkout(user, updatedWorkout, workoutId)).thenReturn(updatedWorkout);

        ResponseEntity<WorkoutDto> response = workoutController.editWorkout(userDetails, workoutId, workoutDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Delete a workout by ID should return no content status")
    void deleteWorkout_ShouldReturnNoContentStatus() {
        Long workoutId = 1L;
        doNothing().when(workoutService).deleteWorkout(workoutId);

        ResponseEntity<?> response = workoutController.deleteWorkout(workoutId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Get workout by ID should return workout details")
    void getWorkoutById_ShouldReturnWorkoutDetails() {
        Long workoutId = 1L;
        Workout workout = new Workout();
        when(workoutService.getWorkoutById(workoutId)).thenReturn(workout);

        ResponseEntity<WorkoutDto> response = workoutController.getWorkoutById(workoutId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Get calories burned in a time period should return total calories burned")
    void getCaloriesBurnedInTimePeriod_ShouldReturnTotalCaloriesBurned() {
        User user = new User("testUser", "password", UserRole.USER);
        String startTime = "2024-05-01T00:00:00";
        String endTime ="2024-05-07T23:59:59";
        int totalCaloriesBurned = 500;
        when(userService.getUserByLogin(userDetails.getUsername())).thenReturn(user);
        when(workoutService.getCaloriesBurnedInTimePeriod(user, LocalDateTime.parse(startTime), LocalDateTime.parse(endTime))).thenReturn(totalCaloriesBurned);

        ResponseEntity<Integer> response = workoutController.getCaloriesBurnedInTimePeriod(userDetails, startTime, endTime);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(totalCaloriesBurned);
    }

    @Test
    @DisplayName("Get additional parameters statistics for a workout type in a time period should return statistics")
    void getAdditionalParamsStats_ShouldReturnStatistics() {
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutType workoutType = WorkoutType.BALANCE;
        String startTime = "2024-05-01T00:00:00";
        String endTime = "2024-05-07T23:59:59";
        List<WorkoutAdditionalParams> additionalParamsList = new ArrayList<>();
        when(userService.getUserByLogin(userDetails.getUsername())).thenReturn(user);
        when(workoutService.getAdditionalParamsStats(user, workoutType, LocalDateTime.parse(startTime), LocalDateTime.parse(endTime))).thenReturn(additionalParamsList);
        List<WorkoutAdditionalParamsDto> additionalParamsDtoList = paramsMapper.listWorkoutAdditionalParamsToWorkoutAdditionalParamsDto(additionalParamsList);

        ResponseEntity<List<WorkoutAdditionalParamsDto>> response = workoutController.getAdditionalParamsStats(userDetails, workoutType, startTime, endTime);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(additionalParamsDtoList);
    }


    @Test
    @DisplayName("Get all workouts for a specific user should return list of workouts")
    void getAllReadingsWorkouts_ShouldReturnAllWorkouts() {
        User user = new User("testUser", "password", UserRole.ADMIN);
        List<Workout> workoutList = new ArrayList<>();
        when(userService.getUserByLogin(userDetails.getUsername())).thenReturn(user);
        when(workoutService.getAllReadingsWorkouts(user)).thenReturn(workoutList);
        List<WorkoutDto> workoutDtoList = workoutMapper.listWorkoutToWorkoutDto(workoutList);

        ResponseEntity<List<WorkoutDto>> response = workoutController.getAllReadingsWorkouts(userDetails);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(workoutDtoList);
    }
}