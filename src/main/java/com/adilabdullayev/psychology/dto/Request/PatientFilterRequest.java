package com.adilabdullayev.psychology.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = false) // Invalid field throwing error
public class PatientFilterRequest extends BaseFilterRequest{

    private String gender;

}
