package com.ylab.app.repository;

import com.ylab.app.dbService.dao.WorkoutDao;
import com.ylab.app.dbService.dao.impl.WorkoutDaoImpl;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ylab.app.util.TestDataResult.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

/**
 * WorkoutDaoTest class represents the test suite for testing the functionality of the Workout DAO (Data Access Object) class.
 * It utilizes Testcontainers for Docker-based integration testing.
 *
 * @author razlivinsky
 * @since 15.04.2024
 */
@Testcontainers
public class WorkoutDaoTest {
    private Connection connection;

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(getTestDatabaseVersion())
            .withDatabaseName(getTestDatabaseName())
            .withUsername(getTestUsername())
            .withPassword(getTestPassword());

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
    }

    @Test
    @DisplayName("Inserting a workout should persist it in the database")
    public void testInsertWorkout() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        listParams.add(params);
        LocalDateTime date = LocalDateTime.now();
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);

        workoutDao.insertWorkout(workout);

        assertThat(workout.getId()).isNotNull();

        List<Workout> workouts = workoutDao.findWorkoutsByUserAndDate(user, workout.getDate());
        assertThat(workouts).isNotEmpty();
    }

    @Test
    @DisplayName("Finding workouts by user and date should return correct workouts")
    public void testFindWorkoutsByUserAndDate() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        listParams.add(params);
        LocalDateTime date = LocalDateTime.now();
        Workout workout = new Workout();
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);

        workoutDao.insertWorkout(workout);

        List<Workout> workouts = workoutDao.findWorkoutsByUserAndDate(user, date);

        assertThat(workouts).isNotEmpty();
    }

    @Test
    @DisplayName("Finding all workouts should return a list of workouts")
    public void testFindAllWorkoutList() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        listParams.add(params);
        LocalDateTime date = LocalDateTime.now();
        Workout workout = new Workout();
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);

        workoutDao.insertWorkout(workout);
        List<Workout> workouts = workoutDao.findAllWorkoutList();

        assertThat(workouts).isNotEmpty();
    }

    @Test
    @DisplayName("Editing a workout should update its details")
    public void testEditWorkout() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        listParams.add(params);
        LocalDateTime date = LocalDateTime.now();
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);

        workoutDao.insertWorkout(workout);
        Long workoutId = workout.getId();

        workout.setDuration(600);
        workout.setCaloriesBurned(400);

        workoutDao.editWorkout(workout, workoutId);

        assertThat(workout)
                .isNotNull()
                .hasFieldOrPropertyWithValue("duration", 600)
                .hasFieldOrPropertyWithValue("caloriesBurned", 400);
    }

    @Test
    @DisplayName("Deleting a workout should remove it from the database")
    public void testDeleteWorkout() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("test", "pass", UserRole.USER);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(2L, "jumping", 50L);
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        listParams.add(params);
        LocalDateTime date = LocalDateTime.now();
        Workout workout = new Workout();
        workout.setId(2L);
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);

        workoutDao.insertWorkout(workout);
        Long workoutId = workout.getId();

        workoutDao.deleteWorkout(workoutId);
        List<Workout> workouts = workoutDao.findAllWorkoutList();
        boolean doesNotContainId = workouts.stream()
                .noneMatch(workout1 -> workout1.getId() == workoutId);

        assertTrue(doesNotContainId, "Список не содержит объект с id: " + workoutId);
    }

    @Test
    @DisplayName("Getting total calories burned by user should return the correct sum")
    public void testGetTotalCaloriesBurnedByUser() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutAdditionalParams params = new WorkoutAdditionalParams(1L, "jumping", 50L);
        List<WorkoutAdditionalParams> listParams = new ArrayList<>();
        listParams.add(params);
        LocalDateTime date = LocalDateTime.now();
        Workout workout = new Workout();
        workout.setType(WorkoutType.AEROBICS);
        workout.setDate(date);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setParams(listParams);
        workout.setUser(user);

        workoutDao.insertWorkout(workout);
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        int totalCalories = workoutDao.getTotalCaloriesBurnedByUser(user, startDate, endDate);

        assertThat(totalCalories).isGreaterThan(0);
    }

    @Test
    @DisplayName("Finding workout parameters by type, user, and date should return correct parameters")
    public void testFindWorkoutParamsByTypeUserAndDate() throws SQLException {
        WorkoutDao workoutDao = new WorkoutDaoImpl();
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutType workoutType = WorkoutType.AEROBICS;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        Workout workout = new Workout();
        workout.setType(workoutType);
        workout.setDate(startDate);
        workout.setDuration(120);
        workout.setCaloriesBurned(333);
        workout.setUser(user);

        WorkoutAdditionalParams params = new WorkoutAdditionalParams();
        params.setParams("jumping");
        params.setValue(50L);
        workout.setParams(Collections.singletonList(params));

        workoutDao.insertWorkout(workout);

        List<WorkoutAdditionalParams> retrievedParams = workoutDao.findWorkoutParamsByTypeUserAndDate(user, workoutType, startDate, endDate);

        assertThat(retrievedParams)
                .isNotEmpty()
                .extracting(WorkoutAdditionalParams::getParams, WorkoutAdditionalParams::getValue)
                .contains(tuple("jumping", 50L));
    }
}