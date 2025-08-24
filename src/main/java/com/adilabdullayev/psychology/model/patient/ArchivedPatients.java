package com.adilabdullayev.psychology.model.patient;

import jakarta.persistence.*;
import lombok.Data;

import com.adilabdullayev.psychology.model.BaseEntity;
import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "archived_patients")
public class ArchivedPatients extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Client informations
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name", nullable = true)
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;


    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "patient_code")
    private String patientCode;

    // enum can be hold as toString() that's why i chose string type hehe
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PatientStatus status;

    @Column(name = "session_count")
    private Integer sessionCount;

    @Column(name = "last_session_date")
    private LocalDateTime lastSessionDate;


    // informations about deletion
    @Column(name = "deletion_reason", length = 500)
    private String deletionReason;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @PrePersist
    protected void onArchive() {
        this.deleted = true;
    }

}
