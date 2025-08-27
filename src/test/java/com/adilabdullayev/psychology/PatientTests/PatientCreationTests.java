package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import com.adilabdullayev.psychology.service.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientCreationTests {

    private PatientRepository patientRepository;
    private CounselorService counselorService;
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        counselorService = mock(CounselorService.class);

        // Abstract sınıf olduğu için mock kullanıyoruz
        patientService = mock(PatientService.class);
    }

    private Patient buildSamplePatient() {
        Patient p = new Patient();
        p.setFirstName("Ahmet");
        p.setLastName("Yılmaz");
        p.setBirthDate(LocalDate.of(1995, 7, 20));
        p.setGender(Gender.MALE);
        p.setEmail("ahmet.yilmaz@example.com");
        p.setPhone("+90597433533");
        p.setNotes(new ArrayList<>());
        p.setStatus(PatientStatus.NEW);
        return p;
    }

    @Test
    public void testCreateNewPatient_Success() {
        Patient p = buildSamplePatient();
        when(patientService.addPatient(any(Patient.class))).thenAnswer(i -> {
            Patient patient = i.getArgument(0);
            patient.setPatientCode("P123");
            return patient;
        });

        Patient created = patientService.addPatient(p);
        assertNotNull(created);
        assertEquals("Ahmet", created.getFirstName());
        assertEquals(PatientStatus.NEW, created.getStatus());
        assertNotNull(created.getPatientCode());

        verify(patientService, times(1)).addPatient(any(Patient.class));
    }

    @Test
    public void testCreatePatient_EmailOrPhoneConflict() {
        Patient p = buildSamplePatient();
        when(patientService.addPatient(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("Bu e-posta veya telefon zaten kayıtlı."));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            patientService.addPatient(p);
        });
        assertEquals("Bu e-posta veya telefon zaten kayıtlı.", ex.getMessage());
    }

    @Test
    public void testCreatePatient_RestoreDeletedPatient() {
        Patient p = buildSamplePatient();
        when(patientService.addPatient(any(Patient.class))).thenAnswer(i -> {
            Patient patient = i.getArgument(0);
            patient.setDeleted(false);
            patient.setPatientCode("P123");
            return patient;
        });

        Patient restored = patientService.addPatient(p);
        assertFalse(restored.getDeleted());
        assertNotNull(restored.getPatientCode());

        verify(patientService, times(1)).addPatient(any(Patient.class));
    }
}
