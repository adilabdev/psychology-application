package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.service.PatientService;
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
public class PatientUpdateTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    private Patient existingPatient;

    @BeforeEach
    public void setUp() {
        patientRepository.deleteAll();

        existingPatient = new Patient();
        existingPatient.setFirstName("Ahmet");
        existingPatient.setLastName("Yılmaz");
        existingPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        existingPatient.setGender("Erkek");
        existingPatient.setEmail("ahmet@example.com");
        existingPatient.setPhone("+90500000001");
        existingPatient.setStatus(PatientStatus.YENI);

        patientService.addPatient(existingPatient);
    }

    @Test
    public void testUpdatePatientAllFields() {
        Patient updated = new Patient();
        updated.setFirstName("Mehmet");
        updated.setLastName("Kaya");
        updated.setBirthDate(LocalDate.of(1985, 5, 5));
        updated.setGender("Erkek");
        updated.setEmail("mehmet@example.com");
        updated.setPhone("+90500000002");
        updated.setStatus(PatientStatus.AKTIF);

        Patient saved = patientService.updatePatient(existingPatient.getId(), updated);

        assertEquals("Mehmet", saved.getFirstName());
        assertEquals("Kaya", saved.getLastName());
        assertEquals(LocalDate.of(1985,5,5), saved.getBirthDate());
        assertEquals("Erkek", saved.getGender());
        assertEquals("mehmet@example.com", saved.getEmail());
        assertEquals("+90500000002", saved.getPhone());
        assertEquals(PatientStatus.AKTIF, saved.getStatus());
    }

    @Test
    public void testUpdatePatientEmailPhoneConflict() {
        // Başka bir hasta ekleyelim
        Patient another = new Patient();
        another.setFirstName("Ali");
        another.setLastName("Veli");
        another.setBirthDate(LocalDate.of(1992, 2, 2));
        another.setGender("Erkek");
        another.setEmail("ali@example.com");
        another.setPhone("+90500000003");
        another.setStatus(PatientStatus.YENI);

        patientService.addPatient(another);

        Patient conflictUpdate = new Patient();
        conflictUpdate.setFirstName("Ahmet2");
        conflictUpdate.setLastName("Yılmaz2");
        conflictUpdate.setBirthDate(LocalDate.of(1991, 3, 3));
        conflictUpdate.setGender("Erkek");
        conflictUpdate.setEmail("ali@example.com"); // overlapping email
        conflictUpdate.setPhone("+90500000004");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patientService.updatePatient(existingPatient.getId(), conflictUpdate);
        });

        assertTrue(exception.getMessage().contains("başka bir danışana ait"));
    }

    @Test
    public void testUpdatePatientAddNotes() {
        UserCounselorNote note = new UserCounselorNote();
        note.setContent("Yeni not");
        note.setNoteOwnerType(null); // To be appointed in the service

        Patient updated = new Patient();
        updated.setNotes(List.of(note));

        Patient saved = patientService.updatePatient(existingPatient.getId(), updated);

        assertNotNull(saved.getNotes());
        assertEquals(1, saved.getNotes().size());
        assertEquals("Yeni not", saved.getNotes().get(0).getContent());
        assertEquals(saved.getId(), saved.getNotes().get(0).getPatient().getId());
    }
}
