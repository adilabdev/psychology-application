package com.adilabdullayev.psychology.service.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

public interface CounselorService {

    List<Counselor> getAll();

    Counselor add(CounselorRequest request);

    CounselorSpecialization getSpecialtyFromString(String specializationStr);

    Counselor getCounselorById(Long counselorId);

    Counselor findCounselorById(Long counselorId);

    List<Counselor> filterCounselors(CounselorFilterRequest filterRequest);

    Integer getNextSequenceForPrefix(String prefix);

    void softDeleteCounselor(Long counselorId);

    List<Counselor> getActiveCounselors();

    List<Counselor> getAllVisibleCounselors();

    Page<Counselor> getPagedActiveCounselors(Pageable pageable);

    Counselor updateCounselor(Long id, CounselorRequest request);

    List<Counselor> searchCounselors(String query);

    Map<String, Object> getSessionInfo(Long counselorId);

    Map<String, Object> getCounselorStatistics();

}
