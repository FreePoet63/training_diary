package com.ylab.app.util;

import static com.ylab.app.util.DataReader.readWorkoutQuery;

/**
 * DataResultWorkoutQuery class provides methods to retrieve workout query details from the resource bundle.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataResultWorkoutQuery {
    private static final String INSERT_WORKOUT_QUERY = "insert.workout";
    private static final String INSERT_WORKOUT_PARAMS_QUERY = "insert.workout_params";
    private static final String EDIT_WORKOUT_QUERY = "update.workout";
    private static final String EDIT_WORKOUT_PARAMS_QUERY = "update.workout_params";
    private static final String DELETE_WORKOUT_QUERY = "delete.workout";
    private static final String DELETE_WORKOUT_PARAMS_QUERY = "delete.workout_params";
    private static final String WORKOUT_USER_ON_DATE_QUERY = "select.workout_by_date";
    private static final String WORKOUT_PARAMS_LIST_QUERY = "select.workout_params_by_id";
    private static final String SELECT_CALORIES_TOTAL = "select.total_calories";
    private static final String SELECT_WORKOUT_PARAMS_ID = "select.workout_params_by_criteria";
    private static final String SELECT_WORKOUT_PARAMS_STATISTIC = "select.workout_param_statistic";
    private static final String SELECT_WORKOUT_LIST = "select.all_workouts";
    private static final String SELECT_WORKOUT_BY_ID = "select.workout_by_id";

    /**
     * Retrieves the insert workout query.
     *
     * @return the insert workout query
     */
    public static String insertWorkoutQuery() {
        return readWorkoutQuery(INSERT_WORKOUT_QUERY);
    }

    /**
     * Retrieves the insert workout params query.
     *
     * @return the insert workout params query
     */
    public static String insertWorkoutParamsQuery() {
        return readWorkoutQuery(INSERT_WORKOUT_PARAMS_QUERY);
    }

    /**
     * Retrieves the workout user on date query.
     *
     * @return the workout user on date query
     */
    public static String getWorkoutUserOnDateQuery() {
        return readWorkoutQuery(WORKOUT_USER_ON_DATE_QUERY);
    }

    /**
     * Retrieves the workout params list query.
     *
     * @return the workout params list query
     */
    public static String getWorkoutParamsListQuery() {
        return readWorkoutQuery(WORKOUT_PARAMS_LIST_QUERY);
    }

    /**
     * Retrieves the edit workout query.
     *
     * @return the edit workout query
     */
    public static String editWorkoutQuery() {
        return readWorkoutQuery(EDIT_WORKOUT_QUERY);
    }

    /**
     * Retrieves the edit workout params query.
     *
     * @return the edit workout params query
     */
    public static String editWorkoutParamsQuery() {
        return readWorkoutQuery(EDIT_WORKOUT_PARAMS_QUERY);
    }

    /**
     * Retrieves the delete workout query.
     *
     * @return the delete workout query
     */
    public static String deleteWorkoutQuery() {
        return readWorkoutQuery(DELETE_WORKOUT_QUERY);
    }

    /**
     * Retrieves the delete workout params query.
     *
     * @return the delete workout params query
     */
    public static String deleteWorkoutParamsQuery() {
        return readWorkoutQuery(DELETE_WORKOUT_PARAMS_QUERY);
    }

    /**
     * Retrieves the select calories total query.
     *
     * @return the select calories total query
     */
    public static String getSelectCaloriesTotal() {
        return readWorkoutQuery(SELECT_CALORIES_TOTAL);
    }

    /**
     * Retrieves the select workout params ID query.
     *
     * @return the select workout params ID query
     */
    public static String getSelectWorkoutParamsId() {
        return readWorkoutQuery(SELECT_WORKOUT_PARAMS_ID);
    }

    /**
     * Retrieves the select workout params statistic query.
     *
     * @return the select workout params statistic query
     */
    public static String getSelectWorkoutParamsStatistic() {
        return readWorkoutQuery(SELECT_WORKOUT_PARAMS_STATISTIC);
    }

    /**
     * Retrieves the select workout list query.
     *
     * @return the select workout list query
     */
    public static String getSelectWorkoutList() {
        return readWorkoutQuery(SELECT_WORKOUT_LIST);
    }

    /**
     * Retrieves the query to select a specific workout by its ID.
     *
     * @return the SQL query to select a workout by its ID
     */
    public static String getSelectWorkoutById() {
        return readWorkoutQuery(SELECT_WORKOUT_BY_ID);
    }
}