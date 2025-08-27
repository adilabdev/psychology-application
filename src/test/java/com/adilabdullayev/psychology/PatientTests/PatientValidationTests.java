package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.service.patient.PatientService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientValidationTests {

    @Autowired
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = new Patient();
        patient.setFirstName("Ahmet");
        patient.setLastName("Yılmaz");
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90500000001");
        patient.setStatus(PatientStatus.NEW);
    }

    @Test
    public void testMandatoryFieldsCannotBeEmpty() {
        patient.setFirstName("");
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            patientService.addPatient(patient);
        });
        assertTrue(exception.getMessage().contains("Ad boş bırakılamaz"));
    }

    @Test
    public void testInvalidEmailFormat() {
        patient.setEmail("invalid-email");
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            patientService.addPatient(patient);
        });
        assertTrue(exception.getMessage().contains("Geçerli bir e-posta adresi"));
    }

    @Test
    void testInvalidPhoneFormat() {
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Ahmet");
        invalidPatient.setLastName("Test");
        invalidPatient.setEmail("ahmet@example.com");
        invalidPatient.setPhone("12345"); // geçersiz format
        invalidPatient.setGender(Gender.MALE);
        invalidPatient.setBirthDate(LocalDate.of(2000,1,1));

        assertThrows(ConstraintViolationException.class, () -> {
            patientService.addPatient(invalidPatient);
        });
    }

    @Test
    void testFutureBirthDate() {
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Ahmet");
        invalidPatient.setLastName("Test");
        invalidPatient.setEmail("ahmet@example.com");
        invalidPatient.setPhone("+90500000001");
        invalidPatient.setGender(Gender.MALE);
        invalidPatient.setBirthDate(LocalDate.now().plusDays(1)); // gelecekte

        assertThrows(ConstraintViolationException.class, () -> {
            patientService.addPatient(invalidPatient);
        });
    }
}
