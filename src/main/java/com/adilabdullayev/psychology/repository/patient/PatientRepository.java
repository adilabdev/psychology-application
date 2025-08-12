package com.adilabdullayev.psychology.repository.patient;

import com.adilabdullayev.psychology.model.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {

    // finds a client by their email address
    Optional<Patient> findByEmail(String email);

    // finds a client by their phone number
    Optional<Patient> findByPhone(String phone);

    // finds a client by either email or phone, regardless of deletion status
    @Query("SELECT p FROM Patient p WHERE p.email = :email OR p.phone = :phone")
    Optional<Patient> findByEmailOrPhone(String email, String phone);   //to get all (deleted and undeleted)

    // finds an active patient by id
    Optional<Patient> findByEmailOrPhoneAndDeletedFalse(String email, String phone); //to get only undeleted

    // returns a paged list of active clients
    Optional<Patient> findByIdAndDeletedFalse(Long id);

    // finds all active clients with pagination
    Page<Patient> findAllByDeletedFalse(Pageable pageable);

    Page<Patient> findByDeletedFalse(Pageable pageable);

    // checks if a client with the given patient code is already exists
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Patient p WHERE p.patientCode = :code")
    boolean existsByPatientCode(@Param("code") String patientCode);

    // finds the maximum sequence number for given patient code prefix
    @Query("SELECT MAX(CAST(SUBSTRING(p.patientCode, LENGTH(:prefix) + 2, 4) AS int)) " +
            "FROM Patient p WHERE p.patientCode LIKE CONCAT(:prefix, '-%')")
    Integer findMaxSequenceByPrefix(@Param("prefix") String prefix);

}
