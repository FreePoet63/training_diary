package com.ylab.app.service;

import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.audit.Audit;
import com.ylab.app.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an auditing mechanism for user actions in the system.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class Audition {
    private List<String> auditLogs = new ArrayList<>();

    /**
     * Records the specified action performed by the user for auditing purposes.
     *
     * @param user   the user performing the action
     * @param action the action being performed
     * @throws UserValidationException if the user is null or if the action is empty or null
     */
    public void auditAction(User user, Audit action) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (action == null) {
            throw new UserValidationException("Invalid action");
        }
        switch (action) {
            case LOGIN:
                auditLogs.add("User '" + user.getName() + "' logged in on " + LocalDateTime.now() + "\n");
                break;

            case LOGOUT:
                auditLogs.add("User '" + user.getName() + "' logged out on " + LocalDateTime.now() + "\n");
                break;

            case ADD_WORKOUT:
                auditLogs.add("User '" + user.getName() + "' added a workout on " + LocalDateTime.now() + "\n");
                break;

            case WORKOUTS_ON_DATE:
                auditLogs.add("User '" + user.getName() + "' viewed workouts on a specific date on " + LocalDateTime.now() + "\n");
                break;

            case EDIT_WORKOUT:
                auditLogs.add("User '" + user.getName() + "' edited a workout on " + LocalDateTime.now() + "\n");
                break;

            case DELETE_WORKOUT:
                auditLogs.add("User '" + user.getName() + "' deleted a workout on " + LocalDateTime.now() + "\n");
                break;

            case STATISTICS_CALORIES:
                auditLogs.add("User '" + user.getName() + "' viewed calorie statistics on " + LocalDateTime.now() + "\n");
                break;

            case STATISTICS_PARAMS:
                auditLogs.add("User '" + user.getName() + "' viewed parameter statistics on " + LocalDateTime.now() + "\n");
                break;

            default:
                auditLogs.add("Unknown action: " + action);
                break;
        }
    }

    /**
     * Retrieves the list of audit logs.
     *
     * @return the list of audit logs
     */
    public List<String> getAuditLogs() {
        return auditLogs;
    }
}