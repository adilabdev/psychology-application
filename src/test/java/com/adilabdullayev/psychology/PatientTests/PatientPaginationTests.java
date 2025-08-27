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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientPaginationTests {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    public void setUp() {
        patientRepository.deleteAll();
        for (int i = 1; i <= 30; i++) {
            Patient patient = new Patient();
            patient.setFirstName("Hasta" + i);
            patient.setLastName("Test");
            patient.setBirthDate(LocalDate.of(1990, 1, i % 28 + 1));
            patient.setGender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE);
            patient.setEmail("hasta" + i + "@example.com");
            patient.setPhone("+9059743300" + i);
            patient.setStatus(PatientStatus.NEW);
            patientService.addPatient(patient);
        }
    }

    @Test
    public void testPaginationFirstPage() {
        Page<Patient> page = patientService.getActivePatients(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
        assertNotNull(page);
        assertEquals(10, page.getContent().size());
        assertEquals(30, page.getTotalElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    public void testPaginationSecondPage() {
        Page<Patient> page = patientService.getActivePatients(PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
        assertNotNull(page);
        assertEquals(10, page.getContent().size());
    }

    @Test
    public void testPaginationLastPage() {
        Page<Patient> page = patientService.getActivePatients(PageRequest.of(2, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
        assertNotNull(page);
        assertEquals(10, page.getContent().size());
    }
}
