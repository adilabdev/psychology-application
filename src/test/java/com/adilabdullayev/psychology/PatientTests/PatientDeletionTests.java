package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.patient.ArchivedPatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.service.PatientService;
import com.adilabdullayev.psychology.service.CounselorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientDeletionTests {

    private PatientRepository patientRepository;
    private ArchivedPatientRepository archivedPatientRepository;
    private UserCounselorNoteRepository noteRepository;
    private ArchivedUserCounselorNoteRepository archivedNoteRepository;
    private CounselorService counselorService;
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        archivedPatientRepository = mock(ArchivedPatientRepository.class);
        noteRepository = mock(UserCounselorNoteRepository.class);
        archivedNoteRepository = mock(ArchivedUserCounselorNoteRepository.class);
        counselorService = mock(CounselorService.class);  // Mock CounselorService

        // Correct constructor for PatientService with all required dependencies
        patientService = new PatientService(
                patientRepository,
                archivedPatientRepository,
                noteRepository,
                archivedNoteRepository,
                counselorService,  // Pass counselorService
                null   // noteService set to null for now
        );
    }

    @Test
    public void testSoftDeletePatientSuccess() {
        // Test to ensure soft delete works correctly
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995, 7, 20));
        patient.setGender("Erkek");
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90597433533");
        patient.setStatus(PatientStatus.YENI);

        // Simulate finding an active patient
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(patient));
        when(archivedPatientRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Soft delete patient
        assertDoesNotThrow(() -> {
            patientService.softDeletePatient(1L, "Test sebep", "Tester", "127.0.0.1");
        });

        // Verify delete and archive actions
        verify(patientRepository, times(1)).delete(patient);
        verify(archivedPatientRepository, times(1)).save(any());
    }

    @Test
    public void testSoftDeletePatientNotFound() {
        // Test when patient does not exist
        when(patientRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        // Try deleting a non-existent patient
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.softDeletePatient(999L, "Sebep", "Tester", "127.0.0.1");
        });

        // Assert exception is thrown with correct message
        assertTrue(ex.getMessage().contains("Hasta bulunamadı"));
    }

    @Test
    public void testSoftDeletedPatientNotReturned() {
        // Test to ensure soft deleted patients are not returned
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setDeleted(true); // Mark the patient as deleted

        when(patientRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.empty()); // Should return empty since the patient is deleted

        // Try retrieving the soft deleted patient
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.findById(2L);
        });

        // Assert exception is thrown with correct message
        assertTrue(ex.getMessage().contains("Patient not found"));
    }
}
