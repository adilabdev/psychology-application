package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.patient.Patient;
import com.adilabdullayev.psychology.model.patient.PatientStatus;
import com.adilabdullayev.psychology.repository.patient.PatientRepository;
import com.adilabdullayev.psychology.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PatientCreationTests {

    private PatientRepository patientRepository;
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientService(patientRepository, null, null, null);
    }

    private Patient buildSamplePatient() {
        Patient p = new Patient();
        p.setFirstName("Ahmet");
        p.setLastName("Yılmaz");
        p.setBirthDate(LocalDate.of(1995, 7, 20));
        p.setGender("Erkek");
        p.setEmail("ahmet.yilmaz@example.com");
        p.setPhone("+90597433533");
        p.setNotes(new ArrayList<>());
        p.setStatus(PatientStatus.YENI);
        return p;
    }

    @Test
    public void testCreateNewPatient_Success() {
        Patient p = buildSamplePatient();

        when(patientRepository.findByEmailOrPhone(anyString(), anyString())).thenReturn(Optional.empty());
        when(patientRepository.existsByPatientCode(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenAnswer(i -> i.getArguments()[0]);

        Patient created = patientService.addPatient(p);

        assertNotNull(created);
        assertEquals("Ahmet", created.getFirstName());
        assertEquals(PatientStatus.YENI, created.getStatus());
        assertNotNull(created.getPatientCode());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testCreatePatient_EmailOrPhoneConflict() {
        Patient p = buildSamplePatient();
        when(patientRepository.findByEmailOrPhone(anyString(), anyString())).thenReturn(Optional.of(p));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            patientService.addPatient(p);
        });

        assertEquals("Bu e-posta veya telefon zaten kayıtlı.", ex.getMessage());
    }

    @Test
    public void testCreatePatient_RestoreDeletedPatient() {
        Patient deletedPatient = buildSamplePatient();
        deletedPatient.setDeleted(true);

        when(patientRepository.findByEmailOrPhone(anyString(), anyString()))
                .thenReturn(Optional.of(deletedPatient));
        when(patientRepository.existsByPatientCode(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenAnswer(i -> i.getArguments()[0]);

        Patient restored = patientService.addPatient(buildSamplePatient());

        assertFalse(restored.getDeleted());
        assertNotNull(restored.getPatientCode());
        verify(patientRepository, times(1)).delete(deletedPatient);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
}
