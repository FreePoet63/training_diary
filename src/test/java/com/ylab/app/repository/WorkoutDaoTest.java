package com.ylab.app.repository;

import com.ylab.app.dbService.dao.impl.WorkoutDaoImpl;
import com.ylab.app.dbService.mappers.WorkoutRowMapper;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ylab.app.util.DataResultWorkoutQuery.deleteWorkoutParamsQuery;
import static com.ylab.app.util.DataResultWorkoutQuery.deleteWorkoutQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * WorkoutDaoTest class represents the test suite for testing the functionality of the Workout DAO (Data Access Object) class.
 *
 * @author razlivinsky
 * @since 15.04.2024
 */
@ExtendWith(MockitoExtension.class)
class WorkoutDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private WorkoutDaoImpl workoutDao;

    @Test
    @DisplayName("Find workouts by user and date should return list of workouts")
    void findWorkoutsByUserAndDate_ShouldReturnListOfWorkouts() {
        User user = new User("testUser", "password", UserRole.USER);
        LocalDateTime targetDate = LocalDateTime.now();
        List<Workout> workoutList = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(WorkoutRowMapper.class), eq(user.getName()), any(Timestamp.class))).thenReturn(workoutList);

        List<Workout> result = workoutDao.findWorkoutsByUserAndDate(user, targetDate);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(workoutList);
    }

    @Test
    @DisplayName("Find workout by ID should return workout or null")
    void findWorkoutById_ShouldReturnWorkoutOrNull() {
        Long workoutId = 1L;
        Workout workout = new Workout();
        when(jdbcTemplate.queryForObject(anyString(), any(WorkoutRowMapper.class), eq(workoutId))).thenReturn(workout);

        Workout result = workoutDao.findWorkoutById(workoutId);

        assertThat(result).isEqualTo(workout);
    }

    @Test
    @DisplayName("Find all workouts should return list of all workouts")
    void findAllWorkoutList_ShouldReturnListOfAllWorkouts() {
        List<Workout> allWorkoutsList = new ArrayList<>();
        when(jdbcTemplate.query(anyString(), any(WorkoutRowMapper.class))).thenReturn(allWorkoutsList);

        List<Workout> result = workoutDao.findAllWorkoutList();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(allWorkoutsList);
    }

    @Test
    @DisplayName("Delete workout should remove workout and its parameters from the database")
    void deleteWorkout_ShouldRemoveWorkoutAndParamsFromDatabase() {
        Long workoutId = 1L;

        workoutDao.deleteWorkout(workoutId);

        verify(jdbcTemplate, times(1)).update(eq(deleteWorkoutParamsQuery()), eq(workoutId));
        verify(jdbcTemplate, times(1)).update(eq(deleteWorkoutQuery()), eq(workoutId));
    }

    @Test
    @DisplayName("Get total calories burned by user in a time period should return total calories")
    void getTotalCaloriesBurnedByUser_ShouldReturnTotalCalories() {
        User user = new User("testUser", "password", UserRole.USER);
        LocalDateTime startDate = LocalDateTime.parse("2024-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2024-01-31T23:59:59");
        int totalCaloriesBurned = 500;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(Object[].class))).thenReturn(totalCaloriesBurned);

        int result = workoutDao.getTotalCaloriesBurnedByUser(user, startDate, endDate);

        assertThat(result).isEqualTo(totalCaloriesBurned);
    }

    @Test
    @DisplayName("Find workout additional parameters by type, user, and date should return a list of parameters")
    void findWorkoutParamsByTypeUserAndDate_ShouldReturnListOfParameters() {
        User user = new User("testUser", "password", UserRole.USER);
        WorkoutType workoutType = WorkoutType.CARDIO;
        LocalDateTime startDate = LocalDateTime.parse("2024-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2024-01-31T23:59:59");
        List<WorkoutAdditionalParams> paramsList = new ArrayList<>();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(paramsList);

        List<WorkoutAdditionalParams> result = workoutDao.findWorkoutParamsByTypeUserAndDate(user, workoutType, startDate, endDate);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(paramsList);
    }
}
