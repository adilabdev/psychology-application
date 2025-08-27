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

    public void logAction(AuditActionType actionType,
                          String entityName,
                          Long entityId,
                          String performedBy,
                          String ipAddress,
                          String description) {

        AuditLog log = new AuditLog();
        log.setActionType(actionType);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setPerformedBy(performedBy);
        log.setIpAddress(ipAddress);
        log.setPerformedAt(LocalDateTime.now());
        log.setDescription(description);

        auditLogRepository.save(log);
    }
}

