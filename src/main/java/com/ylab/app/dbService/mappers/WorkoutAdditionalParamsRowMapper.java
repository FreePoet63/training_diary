package com.ylab.app.dbService.mappers;

import com.ylab.app.model.workout.WorkoutAdditionalParams;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * WorkoutAdditionalParamsRowMapper class is responsible for mapping rows from a ResultSet to WorkoutAdditionalParams instances.
 * This class implements the Spring RowMapper interface to customize the mapping process for WorkoutAdditionalParams objects.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Component
public class WorkoutAdditionalParamsRowMapper implements RowMapper<WorkoutAdditionalParams>{

    /**
     * Maps a row of the ResultSet to a WorkoutAdditionalParams object.
     *
     * @param rs     the ResultSet, pointing to the current row being mapped
     * @param rowNum the number of the current row
     * @return a WorkoutAdditionalParams object with data fetched from the ResultSet
     * @throws SQLException if a database access error occurs or if other errors happen while processing the ResultSet
     */
    @Override
    public WorkoutAdditionalParams mapRow(ResultSet rs, int rowNum) throws SQLException {
        WorkoutAdditionalParams param = new WorkoutAdditionalParams();
        param.setId(rs.getLong("workout_id"));
        param.setParams(rs.getString("param"));
        param.setValue(rs.getLong("value"));
        return param;
    }
}