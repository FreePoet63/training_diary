package com.ylab.app.web.controller;

import com.ylab.app.aspect.EnableLogging;
import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WorkoutService;
import com.ylab.app.web.dto.WorkoutAdditionalParamsDto;
import com.ylab.app.web.dto.WorkoutDto;
import com.ylab.app.web.mapper.WorkoutAdditionalParamsMapper;
import com.ylab.app.web.mapper.WorkoutMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class responsible for managing workout-related operations.
 * This class provides endpoints to handle workout creation, retrieval, modification, and deletion.
 *
 * @author razlivinsky
 * @since 01.05.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/workout")
@Validated
@Tag(name = "Workout Controller", description = "Workout API")
@EnableLogging
public class WorkoutController {
    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final UserService userService;
    private final WorkoutAdditionalParamsMapper additionalParamsMapper;

    /**
     * Add a new workout.
     *
     * @param userDetails User who is adding the workout
     * @param workoutDto Workout details to be added
     * @return ResponseEntity containing the added workout details
     */
    @PostMapping("/")
    public ResponseEntity<WorkoutDto> addWorkout(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody WorkoutDto workoutDto) {
        User user = userService.getUserByLogin(userDetails.getUsername());
        Workout workout = workoutMapper.workoutDtoToWorkout(workoutDto);
        Workout createWorkout = workoutService.addWorkout(user, workout);
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.workoutToWorkoutDto(createWorkout));
    }

    /**
     * Get all workouts for a specific user on a given date.
     *
     * @param userDetails User of the user
     * @param targetDate Date for which workshops are queried
     * @return ResponseEntity with a list of workouts
     */
    @GetMapping("/date/{targetDate}")
    public ResponseEntity<List<WorkoutDto>> getWorkoutsOnDate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String targetDate) {
        User user = userService.getUserByLogin(userDetails.getUsername());
        LocalDateTime date = LocalDateTime.parse(targetDate);
        List<Workout> workoutList = workoutService.getWorkoutsOnDate(user, date);
        List<WorkoutDto> workoutDtoList = workoutMapper.listWorkoutToWorkoutDto(workoutList);
        return ResponseEntity.ok(workoutDtoList);
    }

    /**
     * Edit an existing workout.
     *
     * @param workoutId ID of the workout to edit
     * @param userDetails User performing the edit
     * @param workoutDto Updated workout details
     * @return ResponseEntity with the updated workout details
     */
    @PutMapping("/{workoutId}")
    public ResponseEntity<WorkoutDto> editWorkout(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long workoutId, @Validated @RequestBody WorkoutDto workoutDto) {
        User user = userService.getUserByLogin(userDetails.getUsername());
        Workout workout = workoutMapper.workoutDtoToWorkout(workoutDto);
        Workout updateWorkout = workoutService.editWorkout(user, workout, workoutId);
        return ResponseEntity.ok(workoutMapper.workoutToWorkoutDto(updateWorkout));
    }

    /**
     * Delete a workout by its ID.
     *
     * @param workoutId ID of the workout to delete
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long workoutId) {
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get workout by ID.
     *
     * @param workoutId ID of the workout to retrieve
     * @return ResponseEntity containing the requested workout
     */
    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutDto> getWorkoutById(@PathVariable Long workoutId) {
        Workout workout = workoutService.getWorkoutById(workoutId);
        WorkoutDto workoutDto = workoutMapper.workoutToWorkoutDto(workout);
        return ResponseEntity.ok(workoutDto);
    }

    /**
     * Get the total calories burned by a user during a specific time period.
     *
     * @param userDetails the authenticated user requesting the information
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     * @return ResponseEntity with the total calories burned by the user during the specified time period
     */
    @GetMapping("/startDate/{startDate}/endDate/{endDate}")
    public ResponseEntity<Integer> getCaloriesBurnedInTimePeriod(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String startDate,
            @PathVariable String endDate) {
        User user = userService.getUserByLogin(userDetails.getUsername());
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return ResponseEntity.ok(workoutService.getCaloriesBurnedInTimePeriod(user, start, end));
    }

    /**
     * Get additional parameters statistics for a specific workout type within a time period.
     *
     * @param userDetails the authenticated user requesting the statistics
     * @param type the type of workout for which additional parameters are being queried
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     * @return ResponseEntity with a list of additional parameters statistics for the specified workout type
     */
    @GetMapping("/type/{type}/startDate/{startDate}/endDate/{endDate}")
    public ResponseEntity<List<WorkoutAdditionalParamsDto>> getAdditionalParamsStats(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable WorkoutType type,
            @PathVariable String startDate,
            @PathVariable String endDate) {
        User user = userService.getUserByLogin(userDetails.getUsername());
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<WorkoutAdditionalParams> additionalParamsList = workoutService.getAdditionalParamsStats(user, type, start, end);
        List<WorkoutAdditionalParamsDto> additionalParamsDtoList = additionalParamsMapper.listWorkoutAdditionalParamsToWorkoutAdditionalParamsDto(additionalParamsList);
        return ResponseEntity.ok(additionalParamsDtoList);
    }

    /**
     * Get all workouts for a specific user admin.
     *
     * @param userDetails the authenticated user requesting the information
     * @return ResponseEntity with a list of all workouts for the specified user
     */
    @GetMapping("/all/workout")
    public ResponseEntity<List<WorkoutDto>> getAllReadingsWorkouts(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByLogin(userDetails.getUsername());
        List<Workout> workoutList = workoutService.getAllReadingsWorkouts(user);
        List<WorkoutDto> workoutDtoList = workoutMapper.listWorkoutToWorkoutDto(workoutList);
        return ResponseEntity.ok(workoutDtoList);
    }
}