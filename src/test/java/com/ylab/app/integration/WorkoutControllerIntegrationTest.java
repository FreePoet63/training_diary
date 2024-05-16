package com.ylab.app.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.app.config.ApplicationConfig;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WorkoutControllerIntegrationTest class for the workout controller.
 * It focuses on testing the integration of the controller with other components,
 * such as database access, authentication, and request/response handling.
 * The class covers scenarios involving real dependencies and ensures proper
 * interaction between the controller and the rest of the application.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@Import(ApplicationConfig.class)
@WebMvcTest(WorkoutController.class)
class WorkoutControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private WorkoutMapper workoutMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private WorkoutAdditionalParamsMapper additionalParamsMapper;

    private User user;
    private Workout workout;
    private LocalDateTime date;
    private Long workoutId;
    private WorkoutAdditionalParams params;
    private WorkoutDto workoutDto;
    private WorkoutAdditionalParamsDto paramsDto;
    private List<Workout> workoutList;
    private List<WorkoutDto> workoutDtos;
    private List<WorkoutAdditionalParams> listParams;
    private List<WorkoutAdditionalParamsDto> paramsDtos;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        user = new User("test", "test", UserRole.USER);
        params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        workoutList = new ArrayList<>();
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
        workoutList.add(workout);
        workoutId = 1L;
        paramsDtos = new ArrayList<>();
        workoutDtos = new ArrayList<>();
        workoutDtos.add(workoutDto);
        workoutDto = new WorkoutDto(1L, WorkoutType.AEROBICS, date, 100, 100, paramsDtos);
        paramsDto = new WorkoutAdditionalParamsDto(1L, "jumping", 50L);
        mapper.registerModule(new JavaTimeModule());

        when(userService.getUserByLogin(anyString())).thenReturn(user);
        when(workoutMapper.workoutDtoToWorkout(any(WorkoutDto.class))).thenReturn(workout);
        when(workoutService.addWorkout(any(User.class), any(Workout.class))).thenReturn(workout);
        when(workoutService.getWorkoutsOnDate(any(User.class), any(LocalDateTime.class))).thenReturn(workoutList);
        when(workoutService.editWorkout(any(User.class), any(Workout.class), anyLong())).thenReturn(workout);
        when(workoutService.getWorkoutById(anyLong())).thenReturn(workout);
        when(workoutService.getCaloriesBurnedInTimePeriod(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(500);
        when(workoutService.getAllReadingsWorkouts(any(User.class))).thenReturn(workoutList);
        when(workoutMapper.workoutToWorkoutDto(any(Workout.class))).thenReturn(workoutDto);
        when(workoutMapper.listWorkoutToWorkoutDto(anyList())).thenReturn(workoutDtos);
    }

    @Test
    @DisplayName("Add workout successful")
    void whenAddWorkout_thenReturnsCreated() throws Exception {
        mockMvc.perform(post("/workout/")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(workoutDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(workoutDto)));
    }

    @Test
    @DisplayName("Unauthorized user for add workout")
    void whenAddWorkout_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/workout/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(workoutDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get workouts on date is successful")
    void whenGetWorkoutsOnDate_thenReturnsWorkoutList() throws Exception {
        mockMvc.perform(get("/workout/date/{targetDate}", "2024-05-15T22:11")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(workoutDtos)));
    }

    @Test
    @DisplayName("Get bad request on incorrect date format")
    void whenGetWorkoutsOnDate_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/workout/date/{targetDate}", "2024-05-15 22:11")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Workout Successfully")
    void whenEditWorkout_thenReturnsUpdatedWorkout() throws Exception {
        mockMvc.perform(put("/workout/{workoutId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(workoutDto))
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(workoutDto)));
    }

    @Test
    @DisplayName("Delete workout with no content response")
    void whenDeleteWorkout_thenReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/workout/{workoutId}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Fetch specific workout by ID")
    void whenGetWorkoutById_thenReturnsWorkout() throws Exception {
        mockMvc.perform(get("/workout/{workoutId}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(workoutDto)));
    }

    @Test
    @DisplayName("Calculate calories burned in a specified time period")
    void whenGetCaloriesBurnedInTimePeriod_thenReturnsCalories() throws Exception {
        mockMvc.perform(get("/workout/startDate/{startDate}/endDate/{endDate}",
                        "2024-05-01T12:09", "2024-05-15T10:15")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string("500"));
    }

    @Test
    @DisplayName("Retrieve additional parameter stats successfully")
    void whenGetAdditionalParamsStats_thenReturnsStats() throws Exception {
        mockMvc.perform(get("/workout/type/{type}/startDate/{startDate}/endDate/{endDate}",
                        WorkoutType.AEROBICS, "2024-05-01T12:00", "2024-05-15T10:08")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(paramsDtos)));
    }

    @Test
    @DisplayName("Bad request on invalid workout type for stats")
    void whenGetAdditionalParamsStats_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/workout/type/{type}/startDate/{startDate}/endDate/{endDate}",
                        "CROSFIT", "2024-05-01T12:00", "2024-05-15T10:08")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get all readings workouts for admin users")
    void whenGetAllReadingsWorkouts_thenReturnsWorkoutList() throws Exception {
        mockMvc.perform(get("/workout/all/workout")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(workoutDtos)));
    }
}