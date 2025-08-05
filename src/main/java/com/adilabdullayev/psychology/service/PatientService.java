package com.adilabdullayev.psychology.service;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllActive() {
        return patientRepository.findAll(); // @Where anotasyonu filtreleyecek
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient newPatient) {
        Optional<Patient> existingPatientOpt = patientRepository
                .findByEmailOrPhoneAndDeletedFalse(newPatient.getEmail(), newPatient.getPhone());

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
                existingPatient.setUpdatedAt(LocalDateTime.now());

                // Add if there is new notes
                if (newPatient.getNotes() != null) {
                    for (UserCounselorNote note : newPatient.getNotes()) {
                        note.setPatient(existingPatient);
                    }
                    existingPatient.getNotes().clear(); // Önceki notlar silinir (isteğe bağlı)
                    existingPatient.getNotes().addAll(newPatient.getNotes());
                }

                return patientRepository.save(existingPatient);
            } else {
                throw new IllegalArgumentException("Bu e-posta veya telefon zaten kayıtlı.");
            }
        }

        // Yeni kayıt
        newPatient.setDeleted(false);
        newPatient.setCreatedAt(LocalDateTime.now());
        newPatient.setUpdatedAt(LocalDateTime.now());

        if (newPatient.getNotes() != null) {
            for (UserCounselorNote note : newPatient.getNotes()) {
                note.setPatient(newPatient);
            }
        }

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
        existingPatient.setUpdatedAt(LocalDateTime.now());

        // set if there is new notes (if u want u can add new notes during update)
        if (updatedPatient.getNotes() != null) {
            for (UserCounselorNote note : updatedPatient.getNotes()) {
                note.setPatient(existingPatient);
                existingPatient.getNotes().add(note);
            }
        }


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