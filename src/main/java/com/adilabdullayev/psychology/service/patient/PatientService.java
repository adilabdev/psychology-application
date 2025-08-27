package com.adilabdullayev.psychology.service.patient;

import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    Page<Patient> getActivePatients(Pageable pageable);

    List<Patient> getAllPatients();

    Patient findById(Long id);

    List<Patient> filterPatients(PatientFilterRequest filterRequest);

    List<UserCounselorNote> fetchNotesByPatientId(Long patientId);

    List<Patient> searchPatients(String keyword);

    Page<Patient> searchPatients(String keyword, Pageable pageable);

    Patient getPatientById(Long patientId);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPhone(String phone);

    Optional<Patient> findByEmailOrPhone(String email, String phone);

    Page<Patient> findByEmailContainingIgnoreCaseAndFirstNameContainingIgnoreCase(String email, String firstName, Pageable pageable);

    Page<Patient> findAllByDeletedFalse(Pageable pageable);

    Patient addPatient(Patient newPatient);

    Patient updatePatient(Long id, Patient updatedPatient);

    void softDeletePatient(Long id, String reason, String deletedBy, String ipAddress);
}
