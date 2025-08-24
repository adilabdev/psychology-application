package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.service.PatientService;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // test sonunda rollback yapar
public class PatientNoteSearchTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        // clean the db before all tests
        patientRepository.deleteAll();

        patient = new Patient();
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1995, 7, 20));
        patient.setGender("Erkek");
        patient.setEmail("ahmet.test+" + System.currentTimeMillis() + "@example.com"); // unique
        patient.setPhone("+90597433" + (int)(Math.random() * 10000)); // unique
        patient.setStatus(PatientStatus.YENI);

        // add note
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
        boolean found = notes.stream()
                .anyMatch(n -> n.getContent().contains("Pozitif"));
        assertTrue(found);
    }

}
