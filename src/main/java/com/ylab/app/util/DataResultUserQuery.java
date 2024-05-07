package com.ylab.app.util;

import static com.ylab.app.util.DataReader.getUserQuery;

/**
 * DataResultUserQuery class provides methods to retrieve user query details from the resource bundle.
 *
 * @author razlivinsky
 * @since 13.04.2024
 */
public class DataResultUserQuery {
    private static final String INSERT_USER_QUERY = "insert.schema.user";
    private static final String LOGIN_QUERY = "login.user";
    private static final String LIST_USERS = "list.users";
    /**
     * The constant FIND_USER_BY_ID.
     */
    public static final String FIND_USER_BY_ID = "user.id";
    /**
     * The constant FIND_USER_BY_LOGIN.
     */
    public static final String FIND_USER_BY_LOGIN = "user.login";

    /**
     * Retrieves the insert user query.
     *
     * @return the insert user query
     */
    public static String insertUserQuery() {
        return getUserQuery(INSERT_USER_QUERY);
    }

    /**
     * Retrieves the login query.
     *
     * @return the login query
     */
    public static String getLoginQuery() {
        return getUserQuery(LOGIN_QUERY);
    }

    /**
     * Retrieves the list users query.
     *
     * @return the list users query
     */
    public static String getListUsersQuery() {
        return getUserQuery(LIST_USERS);
    }

    /**
     * Gets find user by id.
     *
     * @return the find user by id
     */
    public static String getFindUserById() {
        return getUserQuery(FIND_USER_BY_ID);
    }

    /**
     * Gets find user by login.
     *
     * @return the find user by login
     */
    public static String getFindUserByLogin() {
        return getUserQuery(FIND_USER_BY_LOGIN);
    }
}