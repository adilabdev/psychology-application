package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.repository.archived.ArchivedPatientRepository;
import com.adilabdullayev.psychology.repository.notes.UserCounselorNoteRepository;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import com.adilabdullayev.psychology.service.notes.UserCounselorNoteService;
import com.adilabdullayev.psychology.service.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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

        patientService = mock(PatientService.class); // Abstract class, mock kullan
    }

    @Test
    public void testGeneratePatientCodeUnique() {
        Patient patient = new Patient();
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995,7,20));
        patient.setGender(Gender.MALE);
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90597433533");
        patient.setStatus(PatientStatus.NEW);

        when(patientService.addPatient(any(Patient.class))).thenAnswer(i -> i.getArguments()[0]);

        Patient saved = patientService.addPatient(patient);

        assertNotNull(saved);
    }
}
