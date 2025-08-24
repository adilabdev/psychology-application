package com.adilabdullayev.psychology.dto.Request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CounselorFilterRequest extends BaseFilterRequest {

    private String specialization;
    private Boolean isActive;
    private String role;
}
