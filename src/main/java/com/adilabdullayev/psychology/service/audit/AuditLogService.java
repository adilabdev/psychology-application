package com.adilabdullayev.psychology.service.audit;

import com.adilabdullayev.psychology.model.audit.AuditLog;
import com.adilabdullayev.psychology.model.enums.AuditActionType;
import com.adilabdullayev.psychology.repository.audit.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logAction(String entityName, Long entityId, AuditActionType actionType, String performedBy, String ipAddress, String description) {
        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setActionType(actionType);
        log.setPerformedBy(performedBy);
        log.setIpAddress(ipAddress);
        log.setDescription(description);
        log.setPerformedAt(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}
