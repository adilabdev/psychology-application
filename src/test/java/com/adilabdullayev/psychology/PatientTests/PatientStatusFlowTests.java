package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.patient.PatientStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PatientStatusFlowTests {

    @Test
    public void testStatusTransition() {
        PatientStatus status = PatientStatus.YENI;
        status = PatientStatus.AKTIF;
        assertEquals(PatientStatus.AKTIF, status);
    }
}
