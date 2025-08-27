package com.adilabdullayev.psychology.model.archived;

import com.adilabdullayev.psychology.model.enums.CounselorStatus;
import com.adilabdullayev.psychology.model.notes.BaseNote;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "archived_user_counselor_notes")
public class ArchivedUserCounselorNote extends BaseNote {

    // descriptive metadata about counselor
    @Column(name = "archived_counselor_email", nullable = false)
    private String archivedCounselorEmail;

    @Column(name = "archived_counselor_phone", nullable = false)
    private String archivedCounselorPhone;

    @Column(name = "archived_counselor_code")
    private String archivedCounselorCode;

    @Column(name = "archived_counselor_specialization")
    private String archivedCounselorSpecialization;

    @Column(name = "archived_counselor_first_name")
    private String archivedCounselorFirstName;

    @Column(name = "archived_counselor_last_name")
    private String archivedCounselorLastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "archived_counselor_status")
    private CounselorStatus archivedCounselorStatus;

    // RELATIONSHIP TO ARCHIVED PATIENT
    @ManyToOne
    @JoinColumn(name = "archived_patient_id")  // db colon name
    private ArchivedPatients archivedPatient;

    // Getter & Setter
    public ArchivedPatients getArchivedPatient() {
        return archivedPatient;
    }

    public void setArchivedPatient(ArchivedPatients archivedPatient) {
        this.archivedPatient = archivedPatient;
    }

}