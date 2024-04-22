package com.ylab.app.model.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuditModel class represents an audit message model.
 * This class encapsulates information about an audit message, including its ID and content.
 *
 * @author razlivinsky
 * @since 17.04.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditModel {
    private Long id;
    private String message;

    /**
     * Instantiates a new Audit model with the provided message.
     *
     * @param message The content of the audit message.
     */
    public AuditModel(String message) {
        this.message = message;
    }
}