package com.adilabdullayev.psychology.dto.Response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CounselorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String counselorCode;
    private Boolean isActive;
    private List<String> availableDays;
    private String specialization;
    private String status;
}

