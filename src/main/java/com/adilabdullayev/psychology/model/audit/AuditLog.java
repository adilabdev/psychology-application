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
}
