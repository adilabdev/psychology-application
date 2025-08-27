package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.service.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatientSearchTests {

    @Autowired
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = new Patient();
        patient.setFirstName("Ahmet");
        patient.setLastName("Test");
        patient.setEmail("ahmet@example.com");
        patient.setPhone("+90500000001");
        patient.setBirthDate(java.time.LocalDate.of(1990,1,1));
        patient.setGender(Gender.MALE);

        patientService.addPatient(patient);
    }

    @Test
    public void testSearchByEmail() {
        List<Patient> result = patientService.searchPatients("ahmet@example.com");
        assertFalse(result.isEmpty());
        assertEquals("Ahmet", result.get(0).getFirstName());
    }
}
