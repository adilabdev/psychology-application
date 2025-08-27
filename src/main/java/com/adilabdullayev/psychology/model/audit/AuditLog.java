package com.adilabdullayev.psychology.model.audit;

import com.adilabdullayev.psychology.model.enums.AuditActionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private AuditActionType actionType; // e.g. DELETE, ARCHIVE, UPDATE

    @Column(name = "entity_name", nullable = false)
    private String entityName; // e.g. Patient

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "description", length = 1000)
    private String description;

    public AuditActionType getActionType() {
        return actionType;
    }

    public void setActionType(AuditActionType actionType) {
        this.actionType = actionType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
