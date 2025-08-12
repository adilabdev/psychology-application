package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.patient.ArchivedPatients;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.notes.NoteOwnerType;
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

    // relation with arcihved client (should be only once)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_patient_id", nullable = false)
    private ArchivedPatients archivedPatient;

    // note owner type, default system
    @Enumerated(EnumType.STRING)
    @Column(name = "note_owner_type", nullable = false)
    private NoteOwnerType noteOwnerType = NoteOwnerType.SYSTEM;
    // private NoteOwnerType noteOwnerType; i am using system as a default value, don't forget that

    @Column(name = "note_owner_id")
    private Long noteOwnerId;

    @Column(name = "note_owner_email")
    private String noteOwnerEmail;

    // note content
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // times times times
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_owner_id", insertable = false, updatable = false)
    private Patient noteOwner;
}