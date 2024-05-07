package com.ylab.app.dbService.mappers;

import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.ylab.app.util.DataResultWorkoutQuery.getWorkoutParamsListQuery;

/**
 * WorkoutRowMapper class is responsible for mapping rows from a ResultSet to Workout instances.
 * This class implements the Spring RowMapper interface to customize the mapping process for Workout objects.
 *
 * This class also uses a JdbcTemplate to query and map WorkoutAdditionalParams for each Workout instance.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Component
@RequiredArgsConstructor
public class WorkoutRowMapper implements RowMapper<Workout> {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Maps a row of the ResultSet to a Workout object and retrieves WorkoutAdditionalParams for the mapped Workout.
     *
     * @param rs     the ResultSet, pointing to the current row being mapped
     * @param rowNum  the number of the current row
     * @return a Workout object with data fetched from the ResultSet and its associated WorkoutAdditionalParams
     * @throws SQLException if a database access error occurs or if other errors happen while processing the ResultSet
     */
    @Override
    public Workout mapRow(ResultSet rs, int rowNum) throws SQLException {
        Workout workout = new Workout();
        workout.setId(rs.getLong("id"));
        workout.setType(WorkoutType.valueOf(rs.getString("workout_type")));
        workout.setDate(rs.getTimestamp("date").toLocalDateTime());
        workout.setDuration(rs.getInt("duration"));
        workout.setCaloriesBurned(rs.getInt("calories_burned"));
        String username = rs.getString("user_name");
        User user = new User(username, "", UserRole.USER);
        workout.setUser(user);
        workout.setParams(findParamsByWorkoutId(workout.getId()));
        return workout;
    }

    /**
     * Retrieves WorkoutAdditionalParams for a specific Workout by its ID using JdbcTemplate.
     *
     * @param workoutId the ID of the Workout for which WorkoutAdditionalParams are to be retrieved
     * @return a list of WorkoutAdditionalParams associated with the specified Workout
     */
    private List<WorkoutAdditionalParams> findParamsByWorkoutId(Long workoutId) {
        return jdbcTemplate.query(getWorkoutParamsListQuery(),
                new WorkoutAdditionalParamsRowMapper(),
                workoutId);
    }
}