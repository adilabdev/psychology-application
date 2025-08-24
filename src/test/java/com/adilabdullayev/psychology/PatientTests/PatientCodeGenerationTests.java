package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.patient.ArchivedPatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.service.PatientService;
import com.adilabdullayev.psychology.service.CounselorService;
import com.adilabdullayev.psychology.service.notes.UserCounselorNoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientCodeGenerationTests {

    private PatientRepository patientRepository;
    private ArchivedPatientRepository archivedPatientRepository;
    private UserCounselorNoteRepository noteRepository;
    private ArchivedUserCounselorNoteRepository archivedNoteRepository;
    private CounselorService counselorService;
    private UserCounselorNoteService userCounselorNoteService;
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        archivedPatientRepository = mock(ArchivedPatientRepository.class);
        noteRepository = mock(UserCounselorNoteRepository.class);
        archivedNoteRepository = mock(ArchivedUserCounselorNoteRepository.class);
        counselorService = mock(CounselorService.class);
        userCounselorNoteService = mock(UserCounselorNoteService.class);

        patientService = new PatientService(
                patientRepository,
                archivedPatientRepository,
                noteRepository,
                archivedNoteRepository,
                counselorService,
                userCounselorNoteService
        );
    }

    @Test
    public void testGeneratePatientCodeUnique() {
        when(patientRepository.existsByPatientCode(anyString())).thenReturn(false);
        when(patientRepository.findMaxSequenceByPrefix(anyString())).thenReturn(null);

        Patient patient = new Patient();
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995, 7, 20));
        patient.setGender("Erkek");
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90597433533");
        patient.setStatus(PatientStatus.YENI);

        Patient savedPatient = patientService.addPatient(patient);

        assertNotNull(savedPatient.getPatientCode());
        assertTrue(savedPatient.getPatientCode().startsWith("PAT"));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testMultiplePatientsIncrementCode() {
        when(patientRepository.existsByPatientCode(anyString())).thenReturn(false);
        when(patientRepository.findMaxSequenceByPrefix(anyString()))
                .thenReturn(1)
                .thenReturn(2);

        Patient patient1 = new Patient();
        patient1.setFirstName("Ali");
        patient1.setLastName("Kaya");
        patient1.setBirthDate(LocalDate.of(1990, 1, 1));
        patient1.setGender("Erkek");
        patient1.setEmail("ali@example.com");
        patient1.setPhone("+90555555555");
        patient1.setStatus(PatientStatus.YENI);

        Patient patient2 = new Patient();
        patient2.setFirstName("Ayşe");
        patient2.setLastName("Demir");
        patient2.setBirthDate(LocalDate.of(1992, 2, 2));
        patient2.setGender("Kadın");
        patient2.setEmail("ayse@example.com");
        patient2.setPhone("+90555555556");
        patient2.setStatus(PatientStatus.YENI);

        Patient saved1 = patientService.addPatient(patient1);
        Patient saved2 = patientService.addPatient(patient2);

        assertNotEquals(saved1.getPatientCode(), saved2.getPatientCode());
        verify(patientRepository, times(2)).save(any(Patient.class));
    }

}
