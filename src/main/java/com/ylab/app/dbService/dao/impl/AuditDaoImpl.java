package com.ylab.app.dbService.dao.impl;

import com.ylab.app.dbService.connection.ConnectionManager;
import com.ylab.app.dbService.dao.AuditDao;
import com.ylab.app.exception.dbException.DatabaseReadException;
import com.ylab.app.exception.dbException.DatabaseWriteException;
import com.ylab.app.model.audit.AuditModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ylab.app.util.DataResultAuditQuery.getSelectAuditMessage;
import static com.ylab.app.util.DataResultAuditQuery.insertAuditMessage;

/**
 * AuditDaoImpl class represents an implementation of AuditDao interface for managing audit messages.
 * <p>
 * This class utilizes JdbcTemplate to send and retrieve audit messages from the database.
 *
 * @author razlivinsky
 * @since 17.02.2024
 */
public class AuditDaoImpl implements AuditDao {

    /**
     * Sends an audit message to the database and sets the message ID generated by the database.
     *
     * @param audit the audit message to be sent
     * @throws DatabaseWriteException if the message sending fails
     */
    @Override
    public void sendMessage(AuditModel audit) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertAuditMessage(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, audit.getMessage());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    audit.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseWriteException("Failed to send message " + e.getMessage());
        }
    }

    /**
     * Retrieves all audit messages from the database.
     *
     * @return a list of all audit messages
     * @throws DatabaseReadException if the retrieval of audit messages fails
     */
    @Override
    public List<AuditModel> getMessage() {
        List<AuditModel> auditList = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(getSelectAuditMessage())) {
                while (rs.next()) {
                    AuditModel audit = new AuditModel();
                    audit.setId(rs.getLong("id"));
                    audit.setMessage(rs.getString("message"));
                    auditList.add(audit);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseReadException("Failed to retrieve all audit message " + e.getMessage());
        }
        return auditList;
    }
}