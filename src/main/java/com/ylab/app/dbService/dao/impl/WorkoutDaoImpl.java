package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.dao.WorkoutDao;
import com.ylab.app.dbService.mappers.WorkoutAdditionalParamsRowMapper;
import com.ylab.app.dbService.mappers.WorkoutRowMapper;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ylab.app.util.DataResultWorkoutQuery.*;

/**
 * WorkoutDaoImpl class provides the implementation for interacting with workout data in the database.
 *
 * @author razlivinsky
 * @since 02.05.2024
 */
@Repository
@RequiredArgsConstructor
public class WorkoutDaoImpl implements WorkoutDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Inserts a new workout into the database along with its additional parameters.
     *
     * @param workout the workout to insert
     * @throws DatabaseWriteException if an error occurs during the database operation
     */
    @Override
    public void insertWorkout(Workout workout) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertWorkoutQuery(), new String[]{"id"});
                ps.setString(1, String.valueOf(workout.getType()));
                ps.setTimestamp(2, Timestamp.valueOf(workout.getDate()));
                ps.setInt(3, workout.getDuration());
                ps.setInt(4, workout.getCaloriesBurned());
                ps.setString(5, workout.getUser().getName());
                return ps;
            }, keyHolder);
            long workoutId = keyHolder.getKey().longValue();
            workout.setId(workoutId);
            for (WorkoutAdditionalParams params : workout.getParams()) {
                params.setId(workoutId);
                jdbcTemplate.update(insertWorkoutParamsQuery(),
                        params.getId(),
                        params.getParams(),
                        params.getValue());
            }
        } catch (DataAccessException e) {
            throw new DatabaseWriteException("Error inserting workout: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of workouts for a specific user on a given date from the database.
     *
     * @param user the user for whom workouts are being queried
     * @param targetDate the target date for the workouts
     * @return the list of workouts matching the specified user and date criteria
     * @throws DatabaseReadException if an error occurs during the database operation
     */
    @Override
    public List<Workout> findWorkoutsByUserAndDate(User user, LocalDateTime targetDate) {
        try {
            return jdbcTemplate.query(getWorkoutUserOnDateQuery(),
                    new WorkoutRowMapper(jdbcTemplate),
                    user.getName(), Timestamp.valueOf(targetDate));
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
    }

    /**
     * Find a workout by its ID.
     *
     * @param workoutId the ID of the workout to find
     * @return the workout corresponding to the given ID
     * @throws DatabaseReadException if an error occurs during the database operation
     */
    @Override
    public Workout findWorkoutById(Long workoutId) {
        try {
            return jdbcTemplate.queryForObject(getSelectWorkoutById(),
                    new WorkoutRowMapper(jdbcTemplate), workoutId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of all workouts from the database.
     *
     * @return the list of all workouts in the database
     * @throws DatabaseReadException if an error occurs during the database operation
     */
    @Override
    public List<Workout> findAllWorkoutList() {
        try {
            return jdbcTemplate.query(getSelectWorkoutList(),
                    new WorkoutRowMapper(jdbcTemplate));
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
    }

    /**
     * Updates an existing workout in the database.
     *
     * @param updatedWorkout the updated workout details
     * @param workoutId the ID of the workout to update
     * @throws DatabaseWriteException if an error occurs during the database operation
     */
    @Override
    public void editWorkout(Workout updatedWorkout, Long workoutId) {
        try {
            jdbcTemplate.update(editWorkoutQuery(),
                    updatedWorkout.getType().toString(),
                    Timestamp.valueOf(updatedWorkout.getDate()),
                    updatedWorkout.getDuration(),
                    updatedWorkout.getCaloriesBurned(),
                    updatedWorkout.getUser().getName(),
                    workoutId);
            for (WorkoutAdditionalParams params : updatedWorkout.getParams()) {
                jdbcTemplate.update(editWorkoutParamsQuery(),
                        params.getParams(),
                        params.getValue(),
                        workoutId);
            }
        } catch (DataAccessException e) {
            throw new DatabaseWriteException("Error updating workout: " + e.getMessage());
        }
    }

    /**
     * Deletes a workout from the database based on its ID.
     *
     * @param workoutId the ID of the workout to delete
     * @throws DatabaseWriteException if an error occurs during the database operation
     */
    @Override
    public void deleteWorkout(Long workoutId) {
        try {
            jdbcTemplate.update(deleteWorkoutParamsQuery(), workoutId);
            jdbcTemplate.update(deleteWorkoutQuery(), workoutId);
        } catch (DataAccessException e) {
            throw new DatabaseWriteException("Error deleting workout: " + e.getMessage());
        }
    }

    /**
     * Retrieves the total calories burned by a user within a specified time period.
     *
     * @param user      the user for whom calories burned are being queried
     * @param startDate the start date of the time period
     * @param endDate   the end date of the time period
     * @return the total calories burned by the user in the specified time period
     * @throws DatabaseReadException if an SQL Exception occurs during the database operation
     */
    @Override
    public int getTotalCaloriesBurnedByUser(User user, LocalDateTime startDate, LocalDateTime endDate) {
      try {
          return jdbcTemplate.queryForObject(getSelectCaloriesTotal(),
                  Integer.class,
                  user.getName(), Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Error reading total calories: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of workout additional parameters for a specific user, workout type, and date range.
     *
     * @param user        the user for whom the additional parameters are being queried
     * @param workoutType the type of workout for which the parameters are being queried
     * @param startDate   the start date of the time range
     * @param endDate     the end date of the time range
     * @return the list of workout additional parameters matching the specified criteria
     * @throws DatabaseReadException if an error occurs during the database operation
     */
    @Override
    public List<WorkoutAdditionalParams> findWorkoutParamsByTypeUserAndDate(User user, WorkoutType workoutType, LocalDateTime startDate, LocalDateTime endDate) {
        List<WorkoutAdditionalParams> paramsList = new ArrayList<>();
        try {
            List<Long> workoutIds = jdbcTemplate.query(getSelectWorkoutParamsId(),
                    (rs, rowNum) -> rs.getLong("id"),
                    workoutType.toString(), user.getName(), Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));

            for (Long workoutId : workoutIds) {
                paramsList.addAll(jdbcTemplate.query(getSelectWorkoutParamsStatistic(),
                        new WorkoutAdditionalParamsRowMapper(),
                        workoutId));
            }
            return paramsList;
        } catch (DataAccessException e) {
            throw new DatabaseReadException("Error reading workout parameters: " + e.getMessage());
        }
    }
}