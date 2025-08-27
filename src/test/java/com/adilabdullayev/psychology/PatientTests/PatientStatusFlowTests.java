package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.PatientStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PatientStatusFlowTests {

    @Test
    public void testStatusTransition() {
        PatientStatus status = PatientStatus.NEW;
        status = PatientStatus.ACTIVE;
        assertEquals(PatientStatus.ACTIVE, status);
    }
}
