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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientNoteSearchTests {

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
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995, 7, 20));
        patient.setGender(Gender.MALE);
        patient.setEmail("ahmet.test+" + System.currentTimeMillis() + "@example.com");
        patient.setPhone("+90597433" + (int)(Math.random() * 10000));
        patient.setStatus(PatientStatus.NEW);

        UserCounselorNote note = new UserCounselorNote();
        note.setTitle("İlk görüşme öncesi düşünceler");
        note.setContent("Pozitif bakıyorum.");
        note.setPatient(patient);

        patient.getNotes().add(note);
        patient = patientService.addPatient(patient);
    }

    @Test
    public void testFetchNotesByPatientId() {
        List<UserCounselorNote> notes = patientService.fetchNotesByPatientId(patient.getId());
        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals("İlk görüşme öncesi düşünceler", notes.get(0).getTitle());
    }

    @Test
    public void testPatientNotesContentSearch() {
        List<UserCounselorNote> notes = patientService.fetchNotesByPatientId(patient.getId());
        boolean found = notes.stream().anyMatch(n -> n.getContent().contains("Pozitif"));
        assertTrue(found);
    }
}
