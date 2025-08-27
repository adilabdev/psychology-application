package com.adilabdullayev.psychology.dto.Response;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatientResponse {

    private Long id;

    private String patientCode;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthDate;

    private int age; // calculated

    private Gender gender;

    private String phone;

    private String email;

    private PatientStatus status;

    private Integer sessionCount;

    private LocalDateTime lastSessionDate;

    private List<UserCounselorNote> notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
