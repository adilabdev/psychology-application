package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.enums.NoteOwnerType;
import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseNote {

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    private Patient patient;


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


    @Enumerated(EnumType.STRING)
    @Column(name = "note_owner_type", nullable = false)
    @ColumnDefault("'SYSTEM'")
    private NoteOwnerType noteOwnerType = NoteOwnerType.SYSTEM;


    // note owner type, default system
    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false)
    private NoteType noteType;
    // private NoteOwnerType noteOwnerType; for develompment, i use system as a default value, do not forget set that


    @Column(name = "is_visible_to_patient", nullable = false)
    @ColumnDefault("false")
    private Boolean isVisibleToPatient = false;


    // note content
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


    // times times times
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
