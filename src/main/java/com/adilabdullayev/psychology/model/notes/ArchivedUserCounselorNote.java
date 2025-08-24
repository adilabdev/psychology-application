package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.enums.NoteOwnerType;
import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.patient.ArchivedPatients;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "archived_user_counselor_notes")
public class ArchivedUserCounselorNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "counselor_id", nullable = false)
    private Counselor counselor;

    // relation with arcihved client (should be only once)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_patient_id", nullable = false)
    private ArchivedPatients archivedPatient;

    // note owner type, default system
    @Enumerated(EnumType.STRING)
    @Column(name = "note_owner_type", nullable = false)
    private NoteOwnerType noteOwnerType = NoteOwnerType.SYSTEM;
    // private NoteOwnerType noteOwnerType; i am using system as a default value, don't forget that

    @Column(name = "patient_number")
    private String patientNumber;

    @Column(name = "patient_email")
    private String patientEmail;


    @Column(name = "note_owner_id")
    private Long noteOwnerId;

    @Column(name = "note_owner_email")
    private String noteOwnerEmail;

    @Column(name = "note_owner_phone")
    private String noteOwnerPhone;

    // note content
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // times times times
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false)
    private NoteType noteType;


    @Column(name = "is_visible_to_patient", nullable = false)
    private Boolean isVisibleToPatient = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_owner_id", insertable = false, updatable = false)
    private Patient noteOwner;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}