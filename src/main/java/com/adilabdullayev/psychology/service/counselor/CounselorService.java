package com.adilabdullayev.psychology.service.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.counselor.CounselorSpecialization;

import java.util.List;

public interface CounselorService {

    List<Counselor> getAll();

    Counselor add(Counselor counselor);

    CounselorSpecialization getSpecialtyFromString(String specializationStr);

    Counselor getCounselorById(Long counselorId);

    Counselor findCounselorById(Long counselorId);

    List<Counselor> filterCounselors(CounselorFilterRequest filterRequest);
}
