package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.notes.NoteOwnerType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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


    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_owner_type", nullable = false)
    private NoteOwnerType noteOwnerType = NoteOwnerType.SYSTEM;
    // private NoteOwnerType noteOwnerType; for develompment, i use system as a default value, do not forget set that

    @Column(name = "note_owner_id", insertable = true, updatable = true)
    private Long noteOwnerId;

    @Column(name = "note_owner_email")
    private String noteOwnerEmail;


    @Column(name= "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_owner_id", insertable = false, updatable = false)
    @JsonIgnore
    private Patient noteOwner;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
