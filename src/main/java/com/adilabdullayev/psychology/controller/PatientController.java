package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Hasta servisi çalışıyor.");
    }

    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = patientService.getAllActive();
        if (patients == null || patients.isEmpty()) {
            return ResponseEntity.status(404).body("Aktif hasta bulunamadı.");
        }
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient) {
        Patient created = patientService.addPatient(patient);
        if (created == null) {
            return ResponseEntity.status(400).body("Hasta oluşturulamadı.");
        }
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient updated) {
        Patient updatedPatient = patientService.updatePatient(id, updated);
        if (updatedPatient == null) {
            return ResponseEntity.status(404).body("Güncellenecek hasta bulunamadı ya da işlem başarısız.");
        }
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        try {
            patientService.softDelete(id);
            return ResponseEntity.ok("Danışan başarıyla silindi.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hata: Danışan silinemedi. Sebep: " + e.getMessage());
        }
    }
}
