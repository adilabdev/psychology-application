package com.adilabdullayev.psychology.repository.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;

import java.util.List;

public interface CounselorRepositoryCustom {
    List<Counselor> filterCounselors(CounselorFilterRequest filter);
}
