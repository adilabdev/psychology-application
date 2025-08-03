package com.adilabdullayev.psychology.service;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

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

    public Patient addPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()
                || patientRepository.findByPhone(patient.getPhone()).isPresent()) {
            throw new RuntimeException("Bu e-posta veya telefon numarası zaten kayıtlı.");
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updated) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danışan bulunamadı."));

        Optional<Patient> emailOrPhone = patientRepository.findByEmailOrPhone(
                updated.getEmail(), updated.getPhone()
        );

        if (emailOrPhone.isPresent() && !emailOrPhone.get().getId().equals(id)) {
            throw new RuntimeException("Bu e-posta veya telefon numarası başka bir danışana ait.");
        }

        patient.setFirstName(updated.getFirstName());
        patient.setLastName(updated.getLastName());
        patient.setBirthDate(updated.getBirthDate());
        patient.setGender(updated.getGender());
        patient.setPhone(updated.getPhone());
        patient.setEmail(updated.getEmail());
        patient.setUserNote(updated.getUserNote());

        return patientRepository.save(patient);
    }

    public void softDelete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danışan bulunamadı."));
        patient.setDeleted(true);
        patientRepository.save(patient);
    }
}
