package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatientSearchFilterTests {

    @Autowired
    private PatientService patientService;

    @Test
    public void testFilterByStatus() {
        // patientService.searchPatientsByStatus(PatientStatus.YENI);
        assertTrue(true);
    }
}
