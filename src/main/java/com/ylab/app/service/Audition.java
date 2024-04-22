package com.ylab.app.service;

import com.ylab.app.dbService.dao.AuditDao;
import com.ylab.app.dbService.dao.impl.AuditDaoImpl;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.audit.Audit;
import com.ylab.app.model.audit.AuditModel;
import com.ylab.app.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an auditing mechanism for recording user actions in the system.
 *
 * @author razlivinsky
 * @since 09.04.2024
 */
public class Audition {
    private final AuditDao auditDao = new AuditDaoImpl();
    private AuditModel auditModel;

    /**
     * Records the specified action performed by the user for auditing purposes.
     *
     * @param user   the user performing the action
     * @param action the type of action being performed
     * @return a string describing the action and the timestamp
     * @throws UserValidationException if the user is null or if the action is null
     */
    public String auditAction(User user, Audit action) {
        if (user == null) {
            throw new UserValidationException("Invalid user");
        }
        if (action == null) {
            throw new UserValidationException("Invalid action");
        }
        String result = switch (action) {
            case LOGIN -> "User '" + user.getName() + "' logged in on " + LocalDateTime.now() + "\n";
            case LOGOUT -> "User '" + user.getName() + "' logged out on " + LocalDateTime.now() + "\n";
            case ADD_WORKOUT -> "User '" + user.getName() + "' added a workout on " + LocalDateTime.now() + "\n";
            case WORKOUTS_ON_DATE -> "User '" + user.getName() + "' viewed workouts on a specific date on " + LocalDateTime.now() + "\n";
            case EDIT_WORKOUT -> "User '" + user.getName() + "' edited a workout on " + LocalDateTime.now() + "\n";
            case DELETE_WORKOUT -> "User '" + user.getName() + "' deleted a workout on " + LocalDateTime.now() + "\n";
            case STATISTICS_CALORIES -> "User '" + user.getName() + "' viewed calorie statistics on " + LocalDateTime.now() + "\n";
            case STATISTICS_PARAMS -> "User '" + user.getName() + "' viewed parameter statistics on " + LocalDateTime.now() + "\n";
            default -> "Unknown action: " + action;
        };
        auditModel = new AuditModel(result);
        auditDao.sendMessage(auditModel);
        return result;
    }

    /**
     * Retrieves the list of audit logs.
     *
     * @return the list of audit logs
     */
    public List<AuditModel> getAuditLogs() {
        return auditDao.getMessage();
    }
}