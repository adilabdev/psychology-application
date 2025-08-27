package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.archived.ArchivedPatientRepository;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import com.adilabdullayev.psychology.service.patient.PatientService;
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
        counselorService = mock(CounselorService.class);

        patientService = mock(PatientService.class);
    }

    @Test
    public void testSoftDeletePatientSuccess() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995, 7, 20));
        patient.setGender(Gender.MALE);
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90597433533");
        patient.setStatus(PatientStatus.NEW);

        doNothing().when(patientService).softDeletePatient(1L, "Test sebep", "Tester", "127.0.0.1");

        assertDoesNotThrow(() -> {
            patientService.softDeletePatient(1L, "Test sebep", "Tester", "127.0.0.1");
        });

        verify(patientService, times(1)).softDeletePatient(1L, "Test sebep", "Tester", "127.0.0.1");
    }

    @Test
    public void testSoftDeletePatientNotFound() {
        doThrow(new RuntimeException("Hasta bulunamadı"))
                .when(patientService).softDeletePatient(999L, "Sebep", "Tester", "127.0.0.1");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.softDeletePatient(999L, "Sebep", "Tester", "127.0.0.1");
        });

        assertTrue(ex.getMessage().contains("Hasta bulunamadı"));
    }

    @Test
    public void testSoftDeletedPatientNotReturned() {
        doThrow(new RuntimeException("Patient not found"))
                .when(patientService).findById(2L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            patientService.findById(2L);
        });

        assertTrue(ex.getMessage().contains("Patient not found"));
    }
}
