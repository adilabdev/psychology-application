package com.adilabdullayev.psychology.service.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
import com.adilabdullayev.psychology.dto.Response.CounselorResponse;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounselorService {

    List<Counselor> getAll();

    Counselor add(CounselorRequest request);

    Counselor addCounselor(CounselorRequest request, String performedBy, HttpServletRequest requestContext);

    CounselorSpecialization getSpecialtyFromString(String specializationStr);

    CounselorResponse updateCounselor(Long id, CounselorRequest request, String performedBy, HttpServletRequest httpRequest);

    CounselorResponse findCounselorById(Long counselorId);

    List<Counselor> filterCounselors(CounselorFilterRequest filterRequest);

    Integer getNextSequenceForPrefix(String prefix);

    void softDeleteCounselor(Long counselorId, String deletionReason, String deletedBy, HttpServletRequest request);

    List<Counselor> getActiveCounselors();

    List<Counselor> getAllVisibleCounselors();

    Page<Counselor> getPagedActiveCounselors(Pageable pageable);

    List<Counselor> searchCounselors(String query);

    Map<String, Object> getSessionInfo(Long counselorId);

    Map<String, Object> getCounselorStatistics();

    Optional<Counselor> findByEmailOrPhone(String email, String phone);

}
