package com.ylab.app.dbService.mappers;

import com.ylab.app.model.audit.AuditModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AuditRowMapper class is responsible for mapping rows from a ResultSet to AuditModel instances.
 * This class implements the Spring RowMapper interface to customize the mapping process for AuditModel objects.
 *
 * @author razlivinsky
 * @since 30.04.2024
 */
@Component
public class AuditRowMapper implements RowMapper<AuditModel> {

    /**
     * Maps a row of the ResultSet to an AuditModel object.
     *
     * @param rs     the ResultSet, pointing to the current row being mapped
     * @param rowNum the number of the current row
     * @return an AuditModel object with data retrieved from the ResultSet
     * @throws SQLException if a database access error occurs or if other errors happen while processing the ResultSet
     */
    @Override
    public AuditModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuditModel audit = new AuditModel();
        audit.setMessage(rs.getString("message"));
        audit.setId(rs.getLong("id"));
        return audit;
    }
}