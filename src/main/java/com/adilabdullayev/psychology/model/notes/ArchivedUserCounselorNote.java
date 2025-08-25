package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.patient.ArchivedPatients;
import com.adilabdullayev.psychology.model.patient.Patient;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "archived_user_counselor_notes")
public class ArchivedUserCounselorNote extends BaseNote {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_owner_id", insertable = false, updatable = false)
    private Patient noteOwner;
}