package com.ylab.app.model.audit;

/**
 * The Audit enum represents the various audit events related to user actions.
 *
 * @author razlivinsky
 * @since 01.02.2024
 */
public enum Audit {
    LOGIN,
    LOGOUT,
    ADD_WORKOUT,
    WORKOUTS_ON_DATE,
    EDIT_WORKOUT,
    DELETE_WORKOUT,
    STATISTICS_CALORIES,
    STATISTICS_PARAMS
}