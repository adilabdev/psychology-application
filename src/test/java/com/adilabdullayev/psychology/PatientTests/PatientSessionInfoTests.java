package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.service.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientSessionInfoTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patientRepository.deleteAll();

        patient = new Patient();
        patient.setFirstName("Ahmet");
        patient.setLastName("Test");
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setEmail("ahmet.test@example.com");
        patient.setPhone("+90597433533");
        patient.setStatus(PatientStatus.NEW);

        UserCounselorNote note1 = new UserCounselorNote();
        note1.setContent("İlk seans notu");
        note1.setCreatedAt(LocalDateTime.now().minusDays(5));
        note1.setPatient(patient);

        UserCounselorNote note2 = new UserCounselorNote();
        note2.setContent("İkinci seans notu");
        note2.setCreatedAt(LocalDateTime.now().minusDays(2));
        note2.setPatient(patient);

        patient.getNotes().add(note1);
        patient.getNotes().add(note2);

        patientService.addPatient(patient);
    }

    @Test
    public void testPatientSessionInfo() {
        Patient fetched = patientService.findById(patient.getId());
        assertNotNull(fetched);
        assertEquals(2, fetched.getNotes().size());

        assertNotNull(fetched.getNotes().get(0).getContent());
        assertNotNull(fetched.getNotes().get(0).getCreatedAt());
        assertNotNull(fetched.getNotes().get(1).getContent());
        assertNotNull(fetched.getNotes().get(1).getCreatedAt());
    }

    @Test
    public void testDeletedPatientSessionInfo() {
        patientService.softDeletePatient(patient.getId(), "Test silme", "JUnit", "127.0.0.1");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patientService.findById(patient.getId());
        });

        assertTrue(exception.getMessage().contains("Patient not found") ||
                exception.getMessage().contains("Danışan bulunamadı"));
    }
}
