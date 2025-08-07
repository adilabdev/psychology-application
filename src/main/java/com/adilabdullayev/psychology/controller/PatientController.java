package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.service.PatientService;
import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import java.util.stream.Collectors;  // Collectors için
import org.springframework.context.support.DefaultMessageSourceResolvable;  // DefaultMessageSourceResolvable için

import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Map;
import java.util.HashMap;


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
        try {
            Patient created = patientService.addPatient(patient);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hasta oluşturulamadı: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient updated) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, updated);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        try {
            patientService.softDeletePatient(id);
            return ResponseEntity.ok("Danışan başarıyla silindi.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hata: Danışan silinemedi. Sebep: " + e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterPatients(@Valid @RequestBody PatientFilterRequest filterRequest) {

        boolean hasValidField =
                (filterRequest.getFirstName() != null && !filterRequest.getFirstName().isBlank()) ||
                        (filterRequest.getLastName() != null && !filterRequest.getLastName().isBlank()) ||
                        (filterRequest.getGender() != null && !filterRequest.getGender().isBlank()) ||
                        (filterRequest.getBirthYear() != null) ||
                        (filterRequest.getCreatedAfter() != null) ||
                        (filterRequest.getUpdatedBefore() != null) ||
                        (filterRequest.getEmail() != null && !filterRequest.getEmail().isBlank()) ||   // ✅ email kontrolü
                        (filterRequest.getPhone() != null && !filterRequest.getPhone().isBlank());     // ✅ phone kontrolü

        if (!hasValidField) {
            return ResponseEntity.badRequest().body("En az bir geçerli filtre alanı girilmelidir.");
        }

        List<Patient> filtered = patientService.filterPatients(filterRequest);
        return ResponseEntity.ok(filtered);
    }



    @ExceptionHandler({com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
    public ResponseEntity<?> handleUnknownField(com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException ex) {
        String fieldName = ex.getPropertyName();
        String message = "Geçersiz alan: " + fieldName;
        return ResponseEntity.badRequest().body(message);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
