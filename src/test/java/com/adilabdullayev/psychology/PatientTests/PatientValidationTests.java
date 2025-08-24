package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
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
        patient.setGender("Erkek");
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90500000001");
        patient.setStatus(PatientStatus.YENI);
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
        invalidPatient.setGender("Erkek");
        invalidPatient.setBirthDate(LocalDate.of(2000,1,1));

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            patientService.addPatient(invalidPatient);
        });
    }


    @Test
    void testFutureBirthDate() {
        // invalid patient: doğum tarihi gelecekte
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Ahmet");
        invalidPatient.setLastName("Test");
        invalidPatient.setEmail("ahmet@example.com");
        invalidPatient.setPhone("+90500000001");
        invalidPatient.setGender("Erkek");
        invalidPatient.setBirthDate(LocalDate.now().plusDays(1)); // gelecekte

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            patientService.addPatient(invalidPatient);
        });
    }

}
