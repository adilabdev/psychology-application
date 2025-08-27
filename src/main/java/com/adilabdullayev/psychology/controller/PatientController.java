package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.dto.Request.PatientRequest;
import com.adilabdullayev.psychology.dto.Response.PatientResponse;
import com.adilabdullayev.psychology.mapper.PatientMapper;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.service.patient.PatientService;
import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;  // DefaultMessageSourceResolvable için

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;



import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Map;
import java.util.HashMap;


import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/patients")
public class PatientController {


    private final PatientService patientService;
    private final PatientMapper patientMapper;


    public PatientController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    // to get all active clients
    @GetMapping("/active")
    public ResponseEntity<Page<PatientResponse>> getAllActivePatients(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Patient> patients = patientService.getActivePatients(pageable);
        Page<PatientResponse> response = patients.map(patientMapper::toResponse);
        return ResponseEntity.ok(response);
    }


    // to get all clients (active and passive)
    @GetMapping("/all")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        List<PatientResponse> response = patients.stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    // ping endpoint to check if the system works
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Hasta servisi çalışıyor.");
    }

    // to get the client with specific id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.findById(id);
            PatientResponse response = patientMapper.toResponse(patient);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hasta bulunamadı: " + e.getMessage());
        }
    }


    // to create new client
    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        try {
            Patient patient = patientMapper.toEntity(patientRequest);
            Patient created = patientService.addPatient(patient);
            return ResponseEntity.ok(patientMapper.toResponse(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Hasta oluşturulamadı: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bir hata oluştu.");
        }
    }


    // to update an existing client information
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest patientRequest) {
        try {
            Patient existing = patientService.findById(id);
            patientMapper.updateEntity(patientRequest, existing);
            Patient updated = patientService.updatePatient(id, existing);
            return ResponseEntity.ok(patientMapper.toResponse(updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu.");
        }
    }

    // to soft delete a client
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDelete(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "Silme nedeni belirtilmedi.") String reason,
            @RequestParam(required = false, defaultValue = "Bilinmeyen kullanıcı") String deletedBy,
            HttpServletRequest request
    ) {
        try {
            String ipAddress = request.getRemoteAddr();
            patientService.softDeletePatient(id, reason, deletedBy, ipAddress);
            return ResponseEntity.ok("Danışan başarıyla silindi ve arşive taşındı.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Beklenmeyen bir hata oluştu. Danışan silinemedi. Hata: " + e.getMessage());
        }
    }




    // to get clients according filter criteria
    @PostMapping("/filter")
    public ResponseEntity<?> filterPatients(@Valid @RequestBody PatientFilterRequest filterRequest) {
        boolean hasValidField =
                (filterRequest.getFirstName() != null && !filterRequest.getFirstName().isBlank()) ||
                        (filterRequest.getLastName() != null && !filterRequest.getLastName().isBlank()) ||
                        (filterRequest.getGender() != null && !filterRequest.getGender().isBlank()) ||
                        (filterRequest.getBirthYear() != null) ||
                        (filterRequest.getCreatedAfter() != null) ||
                        (filterRequest.getUpdatedBefore() != null) ||
                        (filterRequest.getEmail() != null && !filterRequest.getEmail().isBlank()) ||
                        (filterRequest.getPhone() != null && !filterRequest.getPhone().isBlank());

        if (!hasValidField) {
            return ResponseEntity.badRequest().body("En az bir geçerli filtre alanı girilmelidir.");
        }

        List<Patient> filtered = patientService.filterPatients(filterRequest);
        List<PatientResponse> response = filtered.stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    // exception handler invalid or unrecognized fields
    @ExceptionHandler({com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
    public ResponseEntity<?> handleUnknownField(com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException ex) {
        String fieldName = ex.getPropertyName();
        String message = "Geçersiz alan: " + fieldName;
        return ResponseEntity.badRequest().body(message);
    }


    // to get active clients paged order
    @GetMapping("/paged")
    public ResponseEntity<Page<Patient>> getAllPatientsPaged(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Patient> patients = patientService.getActivePatients(pageable);
        return ResponseEntity.ok(patients);
    }


    // exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
