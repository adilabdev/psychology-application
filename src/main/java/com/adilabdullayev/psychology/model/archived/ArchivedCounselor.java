package com.adilabdullayev.psychology.model.archived;

import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import com.adilabdullayev.psychology.model.enums.CounselorStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "archived_counselors")
public class ArchivedCounselor extends BaseArchivedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String counselorCode;

    @Enumerated(EnumType.STRING)
    private CounselorSpecialization specialization;

    @Enumerated(EnumType.STRING)
    private CounselorStatus status;

    private Integer sessionCount;
    private LocalDateTime lastSessionDate;
}
