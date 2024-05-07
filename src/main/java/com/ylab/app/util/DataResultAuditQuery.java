package com.ylab.app.util;

import static com.ylab.app.util.DataReader.readAuditQuery;

/**
 * DataResultAuditQuery class provides SQL queries for auditing messages.
 * This class contains methods to retrieve pre-defined SQL queries for inserting and selecting audit messages.
 *
 * @author razlivinsky
 * @since 17.04.2024
 */
public class DataResultAuditQuery {
    private static final String INSERT_AUDIT_MESSAGE = "insert.audit";
    private static final String SELECT_AUDIT_MESSAGE = "select.audit";

    /**
     * Retrieves the SQL query for inserting an audit message.
     *
     * @return The SQL query for inserting an audit message.
     */
    public static String insertAuditMessage() {
        return readAuditQuery(INSERT_AUDIT_MESSAGE);
    }

    /**
     * Retrieves the SQL query for selecting audit messages.
     *
     * @return The SQL query for selecting audit messages.
     */
    public static String getSelectAuditMessage() {
        return readAuditQuery(SELECT_AUDIT_MESSAGE);
    }
}