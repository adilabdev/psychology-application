package com.adilabdullayev.psychology.repository;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepositoryCustom {
    List<Patient> filterPatients(PatientFilterRequest filter);
}
