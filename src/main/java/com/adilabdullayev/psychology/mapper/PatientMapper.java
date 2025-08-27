package com.adilabdullayev.psychology.mapper;

import com.adilabdullayev.psychology.dto.Request.PatientRequest;
import com.adilabdullayev.psychology.dto.Response.PatientResponse;
import com.adilabdullayev.psychology.model.patient.Patient;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequest request) {
        Patient patient = new Patient();
        BeanUtils.copyProperties(request, patient);
        return patient;
    }

    public void updateEntity(PatientRequest request, Patient patient) {
        BeanUtils.copyProperties(request, patient, "id", "patientCode", "createdAt", "deleted");
    }

    public PatientResponse toResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        BeanUtils.copyProperties(patient, response);
        response.setAge(patient.getAge());
        return response;
    }
}
