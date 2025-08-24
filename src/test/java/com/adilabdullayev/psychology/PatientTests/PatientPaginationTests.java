package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.service.PatientService;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
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
        // DB temizlenir
        patientRepository.deleteAll();

        // 30 hasta ekle
        for (int i = 1; i <= 30; i++) {
            Patient patient = new Patient();
            patient.setFirstName("Hasta" + i);
            patient.setLastName("Test");
            patient.setBirthDate(LocalDate.of(1990, 1, i % 28 + 1));
            patient.setGender(i % 2 == 0 ? "Erkek" : "Kadın");
            patient.setEmail("hasta" + i + "@example.com");
            patient.setPhone("+9059743300" + i);
            patient.setStatus(PatientStatus.YENI);

            patientService.addPatient(patient);
        }
    }

    @Test
    public void testPaginationFirstPage() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Patient> page = patientService.getActivePatients(pageRequest);

        assertNotNull(page);
        assertEquals(10, page.getContent().size());
        assertEquals(30, page.getTotalElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    public void testPaginationSecondPage() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Patient> page = patientService.getActivePatients(pageRequest);

        assertNotNull(page);
        assertEquals(10, page.getContent().size());
        assertEquals(30, page.getTotalElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    public void testPaginationLastPage() {
        PageRequest pageRequest = PageRequest.of(2, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Patient> page = patientService.getActivePatients(pageRequest);

        assertNotNull(page);
        assertEquals(10, page.getContent().size());
    }
}
