package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.patient.ArchivedPatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.service.PatientService;
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
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        archivedPatientRepository = mock(ArchivedPatientRepository.class);
        noteRepository = mock(UserCounselorNoteRepository.class);
        archivedNoteRepository = mock(ArchivedUserCounselorNoteRepository.class);

        patientService = new PatientService(
                patientRepository,
                archivedPatientRepository,
                noteRepository,
                archivedNoteRepository
        );
    }

    @Test
    public void testSoftDeletePatientSuccess() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995,7,20));
        patient.setGender("Erkek");
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90597433533");
        patient.setStatus(PatientStatus.YENI);

        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(patient));
        when(archivedPatientRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> {
            patientService.softDeletePatient(1L, "Test sebep", "Tester", "127.0.0.1");
        });

        // patientRepository.delete çağrıldı mı?
        verify(patientRepository, times(1)).delete(patient);
        // archivedPatientRepository.save çağrıldı mı?
        verify(archivedPatientRepository, times(1)).save(any());
    }

    @Test
    public void testSoftDeletePatientNotFound() {
        when(patientRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.softDeletePatient(999L, "Sebep", "Tester", "127.0.0.1");
        });

        assertTrue(ex.getMessage().contains("Hasta bulunamadı"));
    }

    @Test
    public void testSoftDeletedPatientNotReturned() {
        // deleted = the patient is set as an deleted true
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setDeleted(true);

        when(patientRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.findById(2L);
        });

        assertTrue(ex.getMessage().contains("Patient not found"));
    }
}
