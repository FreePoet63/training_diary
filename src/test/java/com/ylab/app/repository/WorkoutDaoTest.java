package com.ylab.app.repository;

import com.ylab.app.dbService.dao.impl.WorkoutDaoImpl;
import com.ylab.app.dbService.mappers.WorkoutRowMapper;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ylab.app.util.DataResultWorkoutQuery.deleteWorkoutParamsQuery;
import static com.ylab.app.util.DataResultWorkoutQuery.deleteWorkoutQuery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

/**
 * WorkoutDaoTest class represents the test suite for the Workout DAO (Data Access Object).
 * It verifies the data persistence layer, focusing on workout-related operations.
 * The tests ensure that all CRUD (Create, Read, Update, Delete) operations
 * are executed correctly, and that the DAO responds appropriately under
 * various scenarios, including boundary cases and error conditions.
 * This class is crucial for maintaining the integrity of the workout data
 * within the system.
 *
 * @author razlivinsky
 * @since 16.05.2024
 */
@ExtendWith(MockitoExtension.class)
class WorkoutDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private WorkoutDaoImpl workoutDao;

    private User user;
    private Workout workout;
    private LocalDateTime date;
    private Long workoutId;
    private WorkoutAdditionalParams params;
    private List<Workout> workoutList;
    private List<WorkoutAdditionalParams> listParams;

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
    }

    @Test
    @DisplayName("insert workout a new workout into the database when successful")
    public void insertWorkout_InsertNewWorkout_WhenSuccessful() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        keyHolder.getKeyList().add(keys);

        ArgumentCaptor<PreparedStatementCreator> pscCaptor = ArgumentCaptor.forClass(PreparedStatementCreator.class);
        ArgumentCaptor<KeyHolder> keyHolderCaptor = ArgumentCaptor.forClass(KeyHolder.class);

        when(jdbcTemplate.update(pscCaptor.capture(), keyHolderCaptor.capture())).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1, KeyHolder.class);
            kh.getKeyList().addAll(keyHolder.getKeyList());
            return 1;
        });

        workoutDao.insertWorkout(workout);

        assertThat(workout.getId()).isEqualTo(1L);
        assertThat(keyHolderCaptor.getValue().getKey().longValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Find workouts by user and date should return list of workouts")
    void findWorkoutsByUserAndDate_ShouldReturnListOfWorkouts() {
        LocalDateTime targetDate = LocalDateTime.now();
        when(jdbcTemplate.query(anyString(),
                any(WorkoutRowMapper.class), eq(user.getName()), any(Timestamp.class))).thenReturn(workoutList);

        List<Workout> result = workoutDao.findWorkoutsByUserAndDate(user, targetDate);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(workoutList);
    }

    @Test
    @DisplayName("Find workout by ID should return workout or null")
    void findWorkoutById_ShouldReturnWorkoutOrNull() {
        Long workoutId = 1L;
        when(jdbcTemplate.queryForObject(anyString(), any(WorkoutRowMapper.class), eq(workoutId))).thenReturn(workout);

        Workout result = workoutDao.findWorkoutById(workoutId);

        assertThat(result).isEqualTo(workout);
    }

    @Test
    @DisplayName("Find all workouts should return list of all workouts")
    void findAllWorkoutList_ShouldReturnListOfAllWorkouts() {
        when(jdbcTemplate.query(anyString(), any(WorkoutRowMapper.class))).thenReturn(workoutList);

        List<Workout> result = workoutDao.findAllWorkoutList();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(workoutList);
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

    @Test
    @DisplayName("Update workout throws DatabaseWriteException when retrieval fails")
    public void EditWorkout_ThrowDatabaseWriteException_WhenRetrievalFails() {
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenThrow(new DataAccessException("Database access failure") {});

        Throwable thrown = catchThrowable(() -> workoutDao.editWorkout(workout, workoutId));

        assertThat(thrown)
                .isInstanceOf(DatabaseWriteException.class)
                .hasMessageContaining("Error updating workout");
    }

    @Test
    @DisplayName("getAllWorkout throws DatabaseReadException when retrieval fails")
    public void getAllWorkout_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.query(anyString(), any(WorkoutRowMapper.class)))
                .thenThrow(new DataAccessException("Database access failure") {});

        Throwable thrown = catchThrowable(() -> workoutDao.findAllWorkoutList());
        assertThat(thrown)
                .isInstanceOf(DatabaseReadException.class)
                .hasMessageContaining("Invalid read");
    }

    @Test
    @DisplayName("findWorkoutById throws UserValidationException when user with the given ID is not found")
    public void findWorkoutById_ThrowUserValidationException_WhenUserNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(WorkoutRowMapper.class),
                anyLong())).thenThrow(EmptyResultDataAccessException.class);

        Workout result = workoutDao.findWorkoutById(9L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("findWorkoutById throws DatabaseReadException when retrieval fails")
    public void findWorkoutById_ThrowDatabaseReadException_WhenRetrievalFails() {
        when(jdbcTemplate.queryForObject(anyString(), any(WorkoutRowMapper.class), anyLong()))
                .thenThrow(new DataAccessException("Database access failure") {});

        Throwable thrown = catchThrowable(() -> workoutDao.findWorkoutById(1L));
        assertThat(thrown)
                .isInstanceOf(DatabaseReadException.class)
                .hasMessageContaining("Invalid read");
    }
}
