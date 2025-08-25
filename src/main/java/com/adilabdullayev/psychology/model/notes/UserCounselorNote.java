package com.adilabdullayev.psychology.model.notes;

import com.adilabdullayev.psychology.model.patient.ArchivedPatients;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "notes")
public class UserCounselorNote extends BaseNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;


    @ManyToOne
    @JoinColumn(name = "counselor_id")
    @JsonBackReference
    private Counselor noteOwner;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_patient_id", nullable = false)
    private ArchivedPatients archivedPatient;

}
