package com.adilabdullayev.psychology.repository;

import com.adilabdullayev.psychology.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long>{
}
