package com.adilabdullayev.psychology.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = false) // Invalid field throwing error
public class PatientFilterRequest {

    private String firstName;

    private String lastName;

    private String  phone;

    private String email;

    private String gender;

    private LocalDate birthDate;

    private Integer birthYear; // only year

    private LocalDate createdAfter;

    private LocalDate updatedBefore;
}
