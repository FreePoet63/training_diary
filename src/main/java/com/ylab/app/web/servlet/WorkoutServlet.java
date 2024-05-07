package com.ylab.app.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.exception.workoutException.WorkoutException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import com.ylab.app.service.WorkoutService;
import com.ylab.app.service.impl.WorkoutServiceImpl;
import com.ylab.app.web.dto.UserDto;
import com.ylab.app.web.dto.WorkoutAdditionalParamsDto;
import com.ylab.app.web.dto.WorkoutDto;
import com.ylab.app.web.mapper.UserMapper;
import com.ylab.app.web.mapper.WorkoutAdditionalParamsMapper;
import com.ylab.app.web.mapper.WorkoutMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WorkoutServlet class for handling workout-related HTTP requests.
 * This servlet provides endpoints for retrieving workout data, adding, updating, and deleting workouts.
 *
 * @author razlivinsky
 * @since 18.04.2024
 */
@WebServlet(name = "WorkoutServlet", urlPatterns = {"/workout/*", "/workout/calories/*", "/workout/params/*", "/workout/all/*"})
public class WorkoutServlet extends HttpServlet {
    private WorkoutService workoutService = new WorkoutServiceImpl();
    private ObjectMapper objectMapper = new ObjectMapper();
    private WorkoutMapper workoutMapper = WorkoutMapper.INSTANCE;
    private WorkoutAdditionalParamsMapper workoutAdditionalParamsMapper = WorkoutAdditionalParamsMapper.INSTANCE;

    /**
     * Handles the POST request to add a new workout.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException the servlet exception
     * @throws IOException the io exception
     * @throws WorkoutException the workout exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        User user = getUserFromSession(request, response);
        objectMapper.registerModule(new JavaTimeModule());
        if (user == null) return;

        try {
            Workout workout = parseRequestBody(request, Workout.class);
            Workout savedWorkout = workoutService.addWorkout(user, workout);
            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getWriter(), workoutMapper.workoutToWorkoutDto(savedWorkout));
        } catch (JsonProcessingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error parsing workout data");
        } catch (WorkoutException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Handles the GET request by checking parameters and calling corresponding methods to retrieve workout data.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException the servlet exception
     * @throws IOException the io exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        User user = getUserFromSession(request, response);
        objectMapper.registerModule(new JavaTimeModule());

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        String requestURI = request.getRequestURI();
        if (request.getParameter("date") != null) {
            getDate(request, response, user);
        } else if (requestURI.endsWith("/calories")) {
            getCaloriesBurned(request, response, user);
        } else if (requestURI.endsWith("/params")) {
            getAdditionalParams(request, response, user);
        } else if (requestURI.endsWith("/all")) {
            getAllWorkouts(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
        }

    }

    /**
     * Handles the PUT request to update an existing workout.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException the servlet exception
     * @throws IOException the io exception
     * @throws WorkoutException the workout exception
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        User user = getUserFromSession(request, response);
        objectMapper.registerModule(new JavaTimeModule());
        if (user == null) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid workout ID");
            return;
        }
        long workoutId = Long.parseLong(pathInfo.substring(1));

        try {
            Workout workout = parseRequestBody(request, Workout.class);
            Workout updatedWorkout = workoutService.editWorkout(user, workout, workoutId);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), workoutMapper.workoutToWorkoutDto(updatedWorkout));
        } catch (JsonProcessingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error parsing workout data");
        } catch (WorkoutException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Handles the DELETE request to delete an existing workout.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException the servlet exception
     * @throws IOException the io exception
     * @throws WorkoutException the workout exception
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workoutIdStr = request.getPathInfo();
        if (workoutIdStr == null || workoutIdStr.isEmpty() || workoutIdStr.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Workout ID not provided");
            return;
        }

        Long workoutId = Long.parseLong(workoutIdStr.substring(1));

        Workout existingWorkout = workoutService.getWorkoutById(workoutId);
        if (existingWorkout.getId() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Workout with ID " + workoutId + " not found");
            return;
        }

        try {
            workoutService.deleteWorkout(workoutId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (WorkoutException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Retrieves the workouts for a specific month and sends the response in JSON format.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param user     the user for whom to retrieve the workouts
     * @throws IOException in case of an input/output error
     * @throws WorkoutException in case of an input/output error
     */
    private void getDate(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            LocalDateTime month = LocalDateTime.parse(request.getParameter("date"));
            List<Workout> workoutList = workoutService.getWorkoutsOnDate(user, month);
            List<WorkoutDto> workoutDtos = workoutList.stream()
                    .map(workoutMapper::workoutToWorkoutDto)
                    .collect(Collectors.toList());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), workoutDtos);
        } catch (WorkoutException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Retrieves the total calories burned within a specified time period and sends the response in JSON format.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param user     the user for whom to retrieve the calories burned
     * @throws IOException in case of an input/output error
     * @throws UserValidationException in case of an input/output error
     * @throws WorkoutException in case of an input/output error
     */
    private void getCaloriesBurned(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate"));
            LocalDateTime endDate = LocalDateTime.parse(request.getParameter("endDate"));
            int result = workoutService.getCaloriesBurnedInTimePeriod(user, startDate, endDate);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), result);
        } catch (UserValidationException | WorkoutException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Retrieves additional parameters statistics for a specific workout type within a time period and sends the response in JSON format.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param user     the user for whom to retrieve the additional parameters statistics
     * @throws IOException in case of an input/output error
     * @throws UserValidationException in case of an input/output error
     * @throws WorkoutException in case of an input/output error
     */
    private void getAdditionalParams(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate"));
            LocalDateTime endDate = LocalDateTime.parse(request.getParameter("endDate"));
            WorkoutType type = WorkoutType.valueOf(request.getParameter("type"));
            List<WorkoutAdditionalParams> result = workoutService.getAdditionalParamsStats(user, type, startDate, endDate);
            List<WorkoutAdditionalParamsDto> paramsDtos = result.stream()
                            .map(workoutAdditionalParamsMapper::workoutAdditionalParamsToWorkoutAdditionalParamsDto)
                                    .collect(Collectors.toList());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), paramsDtos);
        } catch (UserValidationException | WorkoutException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Retrieves all workouts for the user and sends the response in JSON format.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param user     the user for whom to retrieve all workouts
     * @throws IOException in case of an input/output error
     * @throws UserValidationException in case of an input/output error
     * @throws WorkoutException in case of an input/output error
     */
    private void getAllWorkouts(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            List<Workout> allReadingsWorkouts = workoutService.getAllReadingsWorkouts(user);
            List<WorkoutDto> allReadingsWorkoutsDto = allReadingsWorkouts.stream()
                            .map(workoutMapper::workoutToWorkoutDto)
                                    .collect(Collectors.toList());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), allReadingsWorkoutsDto);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (WorkoutException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Parses the request body of an HTTP request into an object of the specified class.
     *
     * @param <T>    the type of the object to parse and return
     * @param request the HTTP servlet request containing the request body to be parsed
     * @param clazz   the class of the object to parse the request body into
     * @return the parsed object of the specified class
     * @throws IOException if an I/O error occurs while reading the request body
     */
    private <T> T parseRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            reader.lines().forEach(sb::append);
        }
        return objectMapper.readValue(sb.toString(), clazz);
    }

    /**
     * Retrieves the user object from the session in the HTTP request.
     *
     * @param request  the HTTP servlet request from which to retrieve the user session
     * @param response the HTTP servlet response, used to send error responses if the user is not logged in
     * @return the user object retrieved from the session, or null if the user is not logged in
     * @throws IOException if an I/O error occurs while handling the HTTP request
     */
    private User getUserFromSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        if (userDto == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return null;
        }
        return UserMapper.INSTANCE.userDtoToUser(userDto);
    }
}