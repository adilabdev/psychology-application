package com.adilabdullayev.psychology.PatientTests;

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
        patient1.setGender("Erkek");
        patient1.setEmail("unique@test.com");
        patient1.setPhone("+90500000001");
        patient1.setStatus(PatientStatus.YENI);

        patientService.addPatient(patient1);
    }

    @Test
    public void testUniqueEmailConstraint() {
        Patient duplicateEmailPatient = new Patient();
        duplicateEmailPatient.setFirstName("Ali");
        duplicateEmailPatient.setLastName("Test");
        duplicateEmailPatient.setBirthDate(LocalDate.of(1991, 2, 2));
        duplicateEmailPatient.setGender("Erkek");
        duplicateEmailPatient.setEmail("unique@test.com"); // aynı email
        duplicateEmailPatient.setPhone("+90500000002");
        duplicateEmailPatient.setStatus(PatientStatus.YENI);

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
        duplicatePhonePatient.setGender("Erkek");
        duplicatePhonePatient.setEmail("unique2@test.com");
        duplicatePhonePatient.setPhone("+90500000001"); // aynı telefon
        duplicatePhonePatient.setStatus(PatientStatus.YENI);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            patientService.addPatient(duplicatePhonePatient);
        });

        assertTrue(exception.getMessage().contains("zaten kayıtlı"));
    }

    @Test
    public void testRestoreDeletedPatientWithSameEmailPhone() {
        // Hasta sil
        patientService.softDeletePatient(patient1.getId(), "Test silme", "JUnit", "127.0.0.1");

        // Silinmiş hasta ile aynı email ve phone kullanarak ekleme
        Patient newPatient = new Patient();
        newPatient.setFirstName("Ahmet2");
        newPatient.setLastName("Test2");
        newPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        newPatient.setGender("Erkek");
        newPatient.setEmail("unique@test.com");
        newPatient.setPhone("+90500000001");

        Patient restored = patientService.addPatient(newPatient);
        assertNotNull(restored);
        assertEquals("Ahmet2", restored.getFirstName());
        assertFalse(restored.getDeleted());
    }
}
