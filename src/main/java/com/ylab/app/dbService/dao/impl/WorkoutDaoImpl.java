package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.WorkoutDao;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.user.User;
import com.ylab.app.model.user.UserRole;
import com.ylab.app.model.workout.Workout;
import com.ylab.app.model.workout.WorkoutAdditionalParams;
import com.ylab.app.model.workout.WorkoutType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ylab.app.util.DataResultWorkoutQuery.*;

/**
 * WorkoutDaoImpl class provides the implementation for interacting with workout data in the database.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class WorkoutDaoImpl implements WorkoutDao {

    /**
     * Inserts a new workout into the database along with its additional parameters.
     *
     * @param workout the workout to insert
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public void insertWorkout(Workout workout) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertWorkoutQuery(), Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, String.valueOf(workout.getType()));
            pstmt.setTimestamp(2, Timestamp.valueOf(workout.getDate()));
            pstmt.setInt(3, workout.getDuration());
            pstmt.setInt(4, workout.getCaloriesBurned());
            pstmt.setString(5, workout.getUser().getName());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long workoutId = rs.getLong(1);
                    workout.setId(workoutId);
                    for (WorkoutAdditionalParams params : workout.getParams()) {
                        params.setId(workoutId);
                    }
                } else {
                    throw new DatabaseWriteException("Inserting meter reading failed, no ID obtained.");
                }
            }
            try (PreparedStatement pstmtParams = conn.prepareStatement(insertWorkoutParamsQuery())) {
                for (WorkoutAdditionalParams params : workout.getParams()) {
                    pstmtParams.setLong(1, params.getId());
                    pstmtParams.setString(2, params.getParams());
                    pstmtParams.setLong(3, params.getValue());
                    pstmtParams.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseWriteException("invalid " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of workouts for a specific user on a given date from the database.
     *
     * @param user       the user for whom workouts are being queried
     * @param targetDate the target date for the workouts
     * @return the list of workouts matching the specified user and date criteria
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public List<Workout> findWorkoutsByUserAndDate(User user, LocalDateTime targetDate) throws SQLException {
        List<Workout> workouts = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement workoutStmt = conn.prepareStatement(getWorkoutUserOnDateQuery())) {
            workoutStmt.setString(1, user.getName());
            workoutStmt.setTimestamp(2, Timestamp.valueOf(targetDate));

            ResultSet workoutRs = workoutStmt.executeQuery();
            while (workoutRs.next()) {
                Workout workout = new Workout();
                workout.setId(workoutRs.getLong("id"));
                workout.setType(WorkoutType.valueOf(workoutRs.getString("workout_type")));
                workout.setDate(workoutRs.getTimestamp("date").toLocalDateTime());
                workout.setDuration(workoutRs.getInt("duration"));
                workout.setCaloriesBurned(workoutRs.getInt("calories_burned"));
                workout.setUser(user);
                workout.setParams(findParamsByWorkoutId(workout.getId()));

                workouts.add(workout);
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return workouts;
    }

    /**
     * Find a workout by its ID.
     *
     * @param workoutId the ID of the workout to find
     * @return the workout corresponding to the given ID
     * @throws SQLException if a database access error occurs
     * @throws DatabaseReadException if there is an issue with reading the database
     */
    @Override
    public Workout findWorkoutById(Long workoutId) throws SQLException {
        Workout workout = new Workout();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement workoutStmt = conn.prepareStatement(getSelectWorkoutById())) {
            workoutStmt.setLong(1, workoutId);

            ResultSet workoutRs = workoutStmt.executeQuery();
            while (workoutRs.next()) {
                String username = workoutRs.getString("user_name");
                User user = new User(username, "", UserRole.USER);

                workout.setId(workoutRs.getLong("id"));
                workout.setType(WorkoutType.valueOf(workoutRs.getString("workout_type")));
                workout.setDate(workoutRs.getTimestamp("date").toLocalDateTime());
                workout.setDuration(workoutRs.getInt("duration"));
                workout.setCaloriesBurned(workoutRs.getInt("calories_burned"));
                workout.setUser(user);
                workout.setParams(findParamsByWorkoutId(workout.getId()));
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return workout;
    }

    /**
     * Retrieves a list of all workouts from the database.
     *
     * @return the list of all workouts in the database
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public List<Workout> findAllWorkoutList() throws SQLException {
        List<Workout> workouts = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement workoutStmt = conn.prepareStatement(getSelectWorkoutList())) {

            ResultSet workoutRs = workoutStmt.executeQuery();
            while (workoutRs.next()) {
                String username = workoutRs.getString("user_name");
                User user = new User(username, "", UserRole.USER);

                Workout workout = new Workout();
                workout.setId(workoutRs.getLong("id"));
                workout.setType(WorkoutType.valueOf(workoutRs.getString("workout_type")));
                workout.setDate(workoutRs.getTimestamp("date").toLocalDateTime());
                workout.setDuration(workoutRs.getInt("duration"));
                workout.setCaloriesBurned(workoutRs.getInt("calories_burned"));
                workout.setUser(user);
                workout.setParams(findParamsByWorkoutId(workout.getId()));

                workouts.add(workout);
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return workouts;
    }

    /**
     * Updates an existing workout in the database.
     *
     * @param updatedWorkout the updated workout details
     * @param workoutId      the ID of the workout to update
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public void editWorkout(Workout updatedWorkout, Long workoutId) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(editWorkoutQuery())) {

            pstmt.setString(1, updatedWorkout.getType().toString());
            pstmt.setTimestamp(2, Timestamp.valueOf(updatedWorkout.getDate()));
            pstmt.setInt(3, updatedWorkout.getDuration());
            pstmt.setInt(4, updatedWorkout.getCaloriesBurned());
            pstmt.setString(5, updatedWorkout.getUser().getName());
            pstmt.setLong(6, workoutId);
            pstmt.executeUpdate();

            try (PreparedStatement pstmtParams = conn.prepareStatement(editWorkoutParamsQuery())) {
                for (WorkoutAdditionalParams params : updatedWorkout.getParams()) {
                    pstmtParams.setString(1, params.getParams());
                    pstmtParams.setLong(2, params.getValue());
                    pstmtParams.setLong(3, workoutId);
                    pstmtParams.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseWriteException("Error updating workout: " + e.getMessage());
        }
    }

    /**
     * Deletes a workout from the database based on its ID.
     *
     * @param workoutId the ID of the workout to delete
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public void deleteWorkout(Long workoutId) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            try (PreparedStatement pstmtParams = conn.prepareStatement(deleteWorkoutParamsQuery())) {
                pstmtParams.setLong(1, workoutId);
                pstmtParams.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(deleteWorkoutQuery())) {
                pstmt.setLong(1, workoutId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
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
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public int getTotalCaloriesBurnedByUser(User user, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        int totalCalories = 0;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getSelectCaloriesTotal())) {

            pstmt.setString(1, user.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(3, Timestamp.valueOf(endDate));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalCalories = rs.getInt("TotalCalories");
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Error reading total calories: " + e.getMessage());
        }
        return totalCalories;
    }

    /**
     * Retrieves a list of workout additional parameters for a specific user, workout type, and date range.
     *
     * @param user        the user for whom the additional parameters are being queried
     * @param workoutType the type of workout for which the parameters are being queried
     * @param startDate   the start date of the time range
     * @param endDate     the end date of the time range
     * @return the list of workout additional parameters matching the specified criteria
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    @Override
    public List<WorkoutAdditionalParams> findWorkoutParamsByTypeUserAndDate(User user, WorkoutType workoutType, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<WorkoutAdditionalParams> paramsList = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement workoutStmt = conn.prepareStatement(getSelectWorkoutParamsId());
             PreparedStatement paramsStmt = conn.prepareStatement(getSelectWorkoutParamsStatistic())) {

            workoutStmt.setString(1, workoutType.toString());
            workoutStmt.setString(2, user.getName());
            workoutStmt.setTimestamp(3, Timestamp.valueOf(startDate));
            workoutStmt.setTimestamp(4, Timestamp.valueOf(endDate));

            ResultSet workoutRs = workoutStmt.executeQuery();
            while (workoutRs.next()) {
                long workoutId = workoutRs.getLong("id");

                paramsStmt.setLong(1, workoutId);
                ResultSet paramsRs = paramsStmt.executeQuery();
                while (paramsRs.next()) {
                    WorkoutAdditionalParams param = new WorkoutAdditionalParams();
                    param.setParams(paramsRs.getString("param"));
                    param.setValue(paramsRs.getLong("value"));
                    param.setId(paramsRs.getLong("workout_id"));
                    paramsList.add(param);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Error reading workout parameters: " + e.getMessage());
        }
        return paramsList;
    }

    /**
     * Retrieves the additional parameters of a workout identified by its ID from the database.
     *
     * @param workoutId the ID of the workout for which the additional parameters are being retrieved
     * @return the list of additional parameters of the specified workout
     * @throws SQLException if an SQL Exception occurs during the database operation
     */
    private List<WorkoutAdditionalParams> findParamsByWorkoutId(Long workoutId) throws SQLException {
        List<WorkoutAdditionalParams> params = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement paramsStmt = conn.prepareStatement(getWorkoutParamsListQuery())) {
            paramsStmt.setLong(1, workoutId);

            ResultSet paramsRs = paramsStmt.executeQuery();
            while (paramsRs.next()) {
                WorkoutAdditionalParams param = new WorkoutAdditionalParams();
                param.setParams(paramsRs.getString("param"));
                param.setValue(paramsRs.getLong("value"));
                param.setId(workoutId);

                params.add(param);
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Invalid read " + e.getMessage());
        }
        return params;
    }
}