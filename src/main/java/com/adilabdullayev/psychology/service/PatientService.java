package com.adilabdullayev.psychology.service;

import com.adilabdullayev.psychology.model.Patient;
import com.adilabdullayev.psychology.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;

    public PatientService(PatientRepository repository){
        this.repository=repository;
    }

    public List<Patient> getAllPatients(){
        return repository.findAll();
    }

    public Patient addPatient(Patient patient){
        return repository.save(patient);
    }
}
