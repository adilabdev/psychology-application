package com.adilabdullayev.psychology.model.session;

import com.adilabdullayev.psychology.model.BaseEntity;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sessions")
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation with patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // relation with counselor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private Counselor counselor;

    // session date and hour
    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    // session time (by minute)
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    // automathic time stamps
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}