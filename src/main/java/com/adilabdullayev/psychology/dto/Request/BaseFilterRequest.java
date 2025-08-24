package com.adilabdullayev.psychology.dto.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class BaseFilterRequest {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private LocalDate birthDate;

    private Integer birthYear; // only year

    private LocalDate createdAfter;

    private LocalDate updatedBefore;
}
