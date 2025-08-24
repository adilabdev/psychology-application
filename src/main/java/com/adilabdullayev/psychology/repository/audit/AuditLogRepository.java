package com.adilabdullayev.psychology.repository.audit;

import com.adilabdullayev.psychology.model.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

