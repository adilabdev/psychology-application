package com.adilabdullayev.psychology.service;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


@Service
public class PatientService {
    @Autowired
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllActive() {
        return patientRepository.findAll(); // @Where filtrelemesi çalışacak
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient newPatient) {
        Optional<Patient> existingPatientOpt = patientRepository
                .findByEmailOrPhoneAndDeletedFalse(newPatient.getEmail(), newPatient.getPhone());

        // ceheck if there is deleted user
        Optional<Patient> deletedPatientOpt = patientRepository
                .findByEmailOrPhone(newPatient.getEmail(), newPatient.getPhone());

        if (existingPatientOpt.isPresent()) {
            Patient existingPatient = existingPatientOpt.get();

            if (existingPatient.getDeleted()) {
                // find if there is softdeleted and update
                existingPatient.setDeleted(false);
                existingPatient.setFirstName(newPatient.getFirstName());
                existingPatient.setLastName(newPatient.getLastName());
                existingPatient.setBirthDate(newPatient.getBirthDate());
                existingPatient.setGender(newPatient.getGender());
                existingPatient.setPhone(newPatient.getPhone());
                existingPatient.setEmail(newPatient.getEmail());
                existingPatient.setUserNote(newPatient.getUserNote());
                existingPatient.setAdminNote(newPatient.getAdminNote());
                existingPatient.setUpdatedAt(LocalDateTime.now());

                return patientRepository.save(existingPatient);

            } else {
                // if there is active user throw exception
                throw new IllegalArgumentException("Bu e-posta veya telefon zaten kayıtlı.");
            }
        }
        // add new patient
        newPatient.setDeleted(false);
        newPatient.setCreatedAt(LocalDateTime.now());
        newPatient.setUpdatedAt(LocalDateTime.now());
        return patientRepository.save(newPatient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new RuntimeException("Danışan bulunamadı veya silinmiş."));

        Optional<Patient> conflictPatientOpt = patientRepository.findByEmailOrPhone(
                updatedPatient.getEmail(), updatedPatient.getPhone()
        );

        if (conflictPatientOpt.isPresent() && !conflictPatientOpt.get().getId().equals(id)) {
            throw new RuntimeException("Bu e-posta veya telefon numarası başka bir danışana ait.");
        }

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setBirthDate(updatedPatient.getBirthDate());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setPhone(updatedPatient.getPhone());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setUserNote(updatedPatient.getUserNote());
        existingPatient.setAdminNote(updatedPatient.getAdminNote());
        existingPatient.setUpdatedAt(LocalDateTime.now());

        return patientRepository.save(existingPatient);
    }

    public void softDeletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danışan bulunamadı."));

        patient.setDeleted(true);
        patient.setUpdatedAt(LocalDateTime.now());

        patientRepository.save(patient);
    }

}
