package com.adilabdullayev.psychology.repository;

import com.adilabdullayev.psychology.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPhone(String phone);
    Optional<Patient> findByEmailOrPhone(String email, String phone);   //to get all (deleted and undeleted)
    Optional<Patient> findByEmailOrPhoneAndDeletedFalse(String email, String phone); //to get only undeleted

    boolean existsByPatientCode(String patientCode);

}
