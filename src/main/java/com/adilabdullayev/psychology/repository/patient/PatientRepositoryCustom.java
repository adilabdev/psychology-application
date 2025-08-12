package com.adilabdullayev.psychology.repository.patient;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;

import java.util.List;

public interface PatientRepositoryCustom {
    List<Patient> filterPatients(PatientFilterRequest filter);
}
