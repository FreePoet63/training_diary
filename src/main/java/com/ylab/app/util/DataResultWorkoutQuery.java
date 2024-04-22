package com.ylab.app.util;

import static com.ylab.app.util.DataReader.getWorkoutQuery;

/**
 * DataResultWorkoutQuery class provides methods to retrieve workout query details from the resource bundle.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataResultWorkoutQuery {
    private static final String INSERT_WORKOUT_QUERY = "insert.workout";
    private static final String INSERT_WORKOUT_PARAMS_QUERY = "insert.workout.params";
    private static final String WORKOUT_USER_ON_DATE_QUERY = "workout.date.query";
    private static final String WORKOUT_PARAMS_LIST_QUERY = "workout.params.list";
    private static final String EDIT_WORKOUT_QUERY = "edit.workout";
    private static final String EDIT_WORKOUT_PARAMS_QUERY = "edit.workout.params";
    private static final String DELETE_WORKOUT_QUERY = "delete.workout";
    private static final String DELETE_WORKOUT_PARAMS_QUERY = "delete.workout.params";
    private static final String SELECT_CALORIES_TOTAL = "select.calories.sum";
    private static final String SELECT_WORKOUT_PARAMS_ID = "select.workout.params.id";
    private static final String SELECT_WORKOUT_PARAMS_STATISTIC = "select.workout.params.statistic";
    private static final String SELECT_WORKOUT_LIST = "select.workouts";

    /**
     * Retrieves the insert workout query.
     *
     * @return the insert workout query
     */
    public static String insertWorkoutQuery() {
        return getWorkoutQuery(INSERT_WORKOUT_QUERY);
    }

    /**
     * Retrieves the insert workout params query.
     *
     * @return the insert workout params query
     */
    public static String insertWorkoutParamsQuery() {
        return getWorkoutQuery(INSERT_WORKOUT_PARAMS_QUERY);
    }

    /**
     * Retrieves the workout user on date query.
     *
     * @return the workout user on date query
     */
    public static String getWorkoutUserOnDateQuery() {
        return getWorkoutQuery(WORKOUT_USER_ON_DATE_QUERY);
    }

    /**
     * Retrieves the workout params list query.
     *
     * @return the workout params list query
     */
    public static String getWorkoutParamsListQuery() {
        return getWorkoutQuery(WORKOUT_PARAMS_LIST_QUERY);
    }

    /**
     * Retrieves the edit workout query.
     *
     * @return the edit workout query
     */
    public static String editWorkoutQuery() {
        return getWorkoutQuery(EDIT_WORKOUT_QUERY);
    }

    /**
     * Retrieves the edit workout params query.
     *
     * @return the edit workout params query
     */
    public static String editWorkoutParamsQuery() {
        return getWorkoutQuery(EDIT_WORKOUT_PARAMS_QUERY);
    }

    /**
     * Retrieves the delete workout query.
     *
     * @return the delete workout query
     */
    public static String deleteWorkoutQuery() {
        return getWorkoutQuery(DELETE_WORKOUT_QUERY);
    }

    /**
     * Retrieves the delete workout params query.
     *
     * @return the delete workout params query
     */
    public static String deleteWorkoutParamsQuery() {
        return getWorkoutQuery(DELETE_WORKOUT_PARAMS_QUERY);
    }

    /**
     * Retrieves the select calories total query.
     *
     * @return the select calories total query
     */
    public static String getSelectCaloriesTotal() {
        return getWorkoutQuery(SELECT_CALORIES_TOTAL);
    }

    /**
     * Retrieves the select workout params ID query.
     *
     * @return the select workout params ID query
     */
    public static String getSelectWorkoutParamsId() {
        return getWorkoutQuery(SELECT_WORKOUT_PARAMS_ID);
    }

    /**
     * Retrieves the select workout params statistic query.
     *
     * @return the select workout params statistic query
     */
    public static String getSelectWorkoutParamsStatistic() {
        return getWorkoutQuery(SELECT_WORKOUT_PARAMS_STATISTIC);
    }

    /**
     * Retrieves the select workout list query.
     *
     * @return the select workout list query
     */
    public static String getSelectWorkoutList() {
        return getWorkoutQuery(SELECT_WORKOUT_LIST);
    }
}