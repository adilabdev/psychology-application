package com.adilabdullayev.psychology.model.patient;

import com.adilabdullayev.psychology.model.BaseEntity;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;

@Entity
@Data
// @Where(clause = "deleted = false") // Soft delete ile silinen kayıtları filtrele
@Table(name = "patients")
public class Patient extends PatientBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // counselor and client notes for that specific client
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserCounselorNote> notes = new ArrayList<>();

    // patient no / patient code - must be unique on the system
    @Column(name = "patient_code", unique = true)
    private String patientCode;

    // Patient Status (with enum)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PatientStatus status;

    // session count
    private Integer sessionCount;

    // last session date
    private LocalDateTime lastSessionDate;
}