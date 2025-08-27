package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.service.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientUniqueConstraintTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    private Patient patient1;

    @BeforeEach
    public void setUp() {
        patientRepository.deleteAll();

        patient1 = new Patient();
        patient1.setFirstName("Ahmet");
        patient1.setLastName("Test");
        patient1.setBirthDate(LocalDate.of(1990, 1, 1));
        patient1.setGender(Gender.MALE);
        patient1.setEmail("unique@test.com");
        patient1.setPhone("+90500000001");
        patient1.setStatus(PatientStatus.NEW);

        patientService.addPatient(patient1);
    }

    @Test
    public void testUniqueEmailConstraint() {
        Patient duplicateEmailPatient = new Patient();
        duplicateEmailPatient.setFirstName("Ali");
        duplicateEmailPatient.setLastName("Test");
        duplicateEmailPatient.setBirthDate(LocalDate.of(1991, 2, 2));
        duplicateEmailPatient.setGender(Gender.MALE);
        duplicateEmailPatient.setEmail("unique@test.com"); // aynı email
        duplicateEmailPatient.setPhone("+90500000002");
        duplicateEmailPatient.setStatus(PatientStatus.NEW);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            patientService.addPatient(duplicateEmailPatient);
        });

        assertTrue(exception.getMessage().contains("zaten kayıtlı"));
    }

    @Test
    public void testUniquePhoneConstraint() {
        Patient duplicatePhonePatient = new Patient();
        duplicatePhonePatient.setFirstName("Mehmet");
        duplicatePhonePatient.setLastName("Test");
        duplicatePhonePatient.setBirthDate(LocalDate.of(1992, 3, 3));
        duplicatePhonePatient.setGender(Gender.MALE);
        duplicatePhonePatient.setEmail("unique2@test.com");
        duplicatePhonePatient.setPhone("+90500000001"); // aynı telefon
        duplicatePhonePatient.setStatus(PatientStatus.NEW);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            patientService.addPatient(duplicatePhonePatient);
        });

        assertTrue(exception.getMessage().contains("zaten kayıtlı"));
    }

    @Test
    public void testRestoreDeletedPatientWithSameEmailPhone() {
        patientService.softDeletePatient(patient1.getId(), "Test silme", "JUnit", "127.0.0.1");

        Patient newPatient = new Patient();
        newPatient.setFirstName("Ahmet2");
        newPatient.setLastName("Test2");
        newPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        newPatient.setGender(Gender.MALE);
        newPatient.setEmail("unique@test.com");
        newPatient.setPhone("+90500000001");
        newPatient.setStatus(PatientStatus.NEW);

        Patient restored = patientService.addPatient(newPatient);
        assertNotNull(restored);
        assertEquals("Ahmet2", restored.getFirstName());
        assertFalse(restored.getDeleted());
    }
}
