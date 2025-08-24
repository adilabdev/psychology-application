package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.enums.NoteOwnerType;
import com.adilabdullayev.psychology.model.enums.NoteType;
import com.adilabdullayev.psychology.model.patient.ArchivedPatients;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notes")
public class UserCounselorNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "counselor_id")
    @JsonBackReference
    private Counselor noteOwner;


    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_patient_id", nullable = false)
    private ArchivedPatients archivedPatient;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_owner_type", nullable = false)
    private NoteOwnerType noteOwnerType = NoteOwnerType.SYSTEM;
    // private NoteOwnerType noteOwnerType; for develompment, i use system as a default value, do not forget set that

    @Column(name = "patient_number")
    private String patientNumber;

    @Column(name = "patient_email")
    private String patientEmail;

    @Column(name = "note_owner_id")
    private Long noteOwnerId;

    @Column(name = "note_owner_email")
    private String noteOwnerEmail;


    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false)
    private NoteType noteType;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_visible_to_patient")
    private Boolean isVisibleToPatient = false;

    @PrePersist
    public void prePersist(){
        if (createdAt == null){
            createdAt = LocalDateTime.now();
        }
    }

}
