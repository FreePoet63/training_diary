package com.ylab.app.model.session;

import com.ylab.app.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Session class represents a user session within the system.
 *
 * @author razlivinsky
 * @since 30.01.2024
 */
public class Session {
    private static Session session;

    private Session() {}

    /**
     * Gets the instance of the Session.
     *
     * @return the session instance
     */
    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    private User user;

    /**
     * Gets the user associated with the session.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user for the session.
     *
     * @param user the user to be set
     */
    public void setUser(User user) {
        this.user = user;
    }
}