package com.adilabdullayev.psychology.model.archived;

import com.adilabdullayev.psychology.model.enums.PatientStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "archived_patients")
public class ArchivedPatients extends BaseArchivedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_code")
    private String patientCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PatientStatus status;

    @Column(name = "session_count")
    private Integer sessionCount;

    @Column(name = "last_session_date")
    private LocalDateTime lastSessionDate;

    // Getter & Setter methods

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientCode() { return patientCode; }
    public void setPatientCode(String patientCode) { this.patientCode = patientCode; }

    public PatientStatus getStatus() { return status; }
    public void setStatus(PatientStatus status) { this.status = status; }

    public Integer getSessionCount() { return sessionCount; }
    public void setSessionCount(Integer sessionCount) { this.sessionCount = sessionCount; }

    public LocalDateTime getLastSessionDate() { return lastSessionDate; }
    public void setLastSessionDate(LocalDateTime lastSessionDate) { this.lastSessionDate = lastSessionDate; }
}
