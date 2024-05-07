package com.ylab.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.WorkoutService;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.mapper.UserMapper;
import com.ylab.app.web.servlet.WorkoutServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the WorkoutServlet class. It aims to verify and validate the functionality of the
 * different HTTP request handling methods available in the WorkoutServlet, such as doPost, doPut, doDelete, and doGet,
 * covering various scenarios including adding, updating, deleting, and retrieving workout data.
 *
 * The tests cover positive cases with valid inputs as well as negative cases incorporating invalid inputs, incorrect
 * user states, and exception handling. They ensure the correct behavior of the WorkoutServlet class under different
 * situations.
 * Additionally, the tests also cover error scenarios to confirm proper error responses and exception handling.
 *
 * @author razlivinsky
 * @since 24.04.2024
 */
public class WorkoutServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutServlet workoutServlet;

    private User user;
    private Workout workout;
    private LocalDateTime date;
    private Long workoutId;
    private WorkoutAdditionalParams params;
    private List<WorkoutAdditionalParams> listParams;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    @DisplayName("Should add workout when user is logged in")
    void doPost_ShouldAddWorkout_WhenUserIsLoggedIn() throws Exception {
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        objectMapper.registerModule(new JavaTimeModule());
        String jsonWorkout = objectMapper.writeValueAsString(workout);

        BufferedReader reader = new BufferedReader(new StringReader(jsonWorkout));
        when(request.getReader()).thenReturn(reader);
        when(workoutService.addWorkout(any(User.class), any(Workout.class))).thenReturn(workout);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(workoutService).addWorkout(any(User.class), any(Workout.class));
    }

    @Test
    @DisplayName("Should update workout when user is logged in and workout exists")
    void doPut_ShouldUpdateWorkout_WhenUserIsLoggedInAndWorkoutExists() throws Exception {
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(request.getPathInfo()).thenReturn("/" + workoutId);
        objectMapper.registerModule(new JavaTimeModule());
        String jsonWorkout = objectMapper.writeValueAsString(workout);

        BufferedReader reader = new BufferedReader(new StringReader(jsonWorkout));
        when(request.getReader()).thenReturn(reader);
        when(workoutService.editWorkout(any(User.class), any(Workout.class), anyLong())).thenReturn(workout);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(workoutService).editWorkout(any(User.class), any(Workout.class), anyLong());
    }

    @Test
    @DisplayName("Should delete workout when workout exists")
    void doDelete_ShouldDeleteWorkout_WhenWorkoutExists() throws Exception {
        workout.setId(workoutId);
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(request.getPathInfo()).thenReturn("/" + workoutId);
        when(workoutService.getWorkoutById(anyLong())).thenReturn(workout);
        doNothing().when(workoutService).deleteWorkout(anyLong());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(workoutService).deleteWorkout(anyLong());
    }

    @Test
    @DisplayName("Should get workouts on specific date")
    void doGet_date() throws Exception {
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        LocalDateTime date = LocalDateTime.now();
        when(request.getParameter("date")).thenReturn(date.toString());
        when(workoutService.getWorkoutsOnDate(user, date)).thenReturn(Collections.singletonList(new Workout()));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Should get calories burned in a time period")
    void doGet_caloriesBurned() throws Exception {
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        when(request.getRequestURI()).thenReturn("/workout/calories");
        when(request.getParameter("startDate")).thenReturn(startDate.toString());
        when(request.getParameter("endDate")).thenReturn(endDate.toString());
        when(workoutService.getCaloriesBurnedInTimePeriod(user, startDate, endDate)).thenReturn(500);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Should get additional params stats")
    void doGet_additionalParams() throws Exception {
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        WorkoutType type = WorkoutType.AEROBICS;
        when(request.getRequestURI()).thenReturn("/workout/params");
        when(request.getParameter("startDate")).thenReturn(startDate.toString());
        when(request.getParameter("endDate")).thenReturn(endDate.toString());
        when(request.getParameter("type")).thenReturn(type.name());

        List<WorkoutAdditionalParams> result = Arrays.asList(new WorkoutAdditionalParams(), new WorkoutAdditionalParams());
        when(workoutService.getAdditionalParamsStats(user, type, startDate, endDate)).thenReturn(result);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Should get all workouts")
    void doGet_allWorkouts() throws Exception {
        User user = new User("admin", "admin", UserRole.ADMIN);
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(request.getRequestURI()).thenReturn("/workout/all");
        List<Workout> allWorkouts = Arrays.asList(new Workout(), new Workout());
        when(workoutService.getAllReadingsWorkouts(user)).thenReturn(allWorkouts);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Test get to date with UserValidationException")
    void testGetToDate_UserValidationException() throws Exception {
        User user = null;
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(request.getParameter("date")).thenThrow(new UserValidationException("User not logged in"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        workoutServlet.doGet(request, response);

        verify(response, times(2)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
    }

    @Test
    @DisplayName("Test get all workout history with UserValidationException")
    void testGetAllWorkoutHistory_UserValidationException() throws Exception {
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        when(request.getRequestURI()).thenReturn("/workout/all");
        when(workoutService.getAllReadingsWorkouts(user)).thenThrow(new UserValidationException("User not logged in"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "User not logged in");
    }

    @Test
    @DisplayName("Test get to date with WorkoutException")
    void testGetToDate_WorkoutException() throws Exception {
        workout = null;
        when(request.getSession()).thenReturn(session);
        UserMapper userMapper = UserMapper.INSTANCE;
        UserDto mockedUserDto = userMapper.userToUserDto(user);
        when(session.getAttribute("user")).thenReturn(mockedUserDto);
        LocalDateTime date = LocalDateTime.now();
        when(request.getParameter("date")).thenReturn(date.toString());
        when(workoutService.getWorkoutsOnDate(user, date)).thenThrow(new WorkoutException("Invalid parameters"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        workoutServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
    }
}
