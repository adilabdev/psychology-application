package com.adilabdullayev.psychology.service.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.AvailableDay;
import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import com.adilabdullayev.psychology.repository.counselor.CounselorRepository;
import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
import com.adilabdullayev.psychology.service.audit.AuditLogService;
import com.adilabdullayev.psychology.model.enums.AuditActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CounselorServiceImpl implements CounselorService {

    private final CounselorRepository counselorRepository;
    private final AuditLogService auditLogService;

    // method for bringing all patients
    @Override
    public List<Counselor> getAll() {
        return counselorRepository.findAll();
    }

    // method for adding a new patient
    @Override
    public Counselor add(CounselorRequest request) {
        // Yeni Counselor nesnesi oluştur
        Counselor counselor = new Counselor();

        // basic informations
        counselor.setFirstName(request.getFirstName());
        counselor.setLastName(request.getLastName());
        counselor.setMiddleName(request.getMiddleName());
        counselor.setBirthDate(request.getBirthDate());
        counselor.setPhone(request.getPhone());
        counselor.setEmail(request.getEmail());

        // is active
        counselor.setIsActive(Boolean.TRUE.equals(request.getIsActive()));

        // code generation
        String code = request.getCounselorCode();
        if (code == null || code.isBlank()) {
            String prefix = "CS";
            int next = getNextSequenceForPrefix(prefix);
            code = prefix + "-" + String.format("%04d", next);
        }
        counselor.setCounselorCode(code);

        // specialization
        CounselorSpecialization specialization = getSpecialtyFromString(
                String.valueOf(request.getSpecializationId())
        );
        counselor.setSpecialization(specialization);

        // days
        if (request.getAvailableDays() != null && !request.getAvailableDays().isEmpty()) {
            List<AvailableDay> days = request.getAvailableDays().stream()
                    .map(dayStr -> {
                        try {
                            return AvailableDay.valueOf(dayStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException("Geçersiz gün: " + dayStr);
                        }
                    })
                    .toList();
            counselor.setAvailableDays(days);
        }

        // save to db
        Counselor saved = counselorRepository.save(counselor);

        // Audit logging
        auditLogService.logAction(
                "Counselor",
                saved.getId(),
                AuditActionType.CREATE,
                "system", // performedBy (can be improve) to do
                "127.0.0.1", // ipAddress (can be improve) to do i think
                "Yeni danışman eklendi: " + saved.getFirstName() + " " + saved.getLastName()
        );

        return saved;
    }


    // changes counselor's speciality from string to enum
    @Override
    public CounselorSpecialization getSpecialtyFromString(String specializationStr) {
        try {
            return CounselorSpecialization.valueOf(specializationStr.toUpperCase()); //
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Geçersiz uzmanlık alanı: " + specializationStr);
        }
    }

    // Method to get a Counselor by ID
    @Override
    public Counselor getCounselorById(Long counselorId) {
        return counselorRepository.findById(counselorId)
                .orElseThrow(() -> new IllegalArgumentException("Counselor not found for id: " + counselorId));
    }


    // dinamic filtering
    @Override
    public List<Counselor> filterCounselors(CounselorFilterRequest filterRequest) {
        return counselorRepository.filterCounselors(filterRequest);
    }

    @Override
    public Integer getNextSequenceForPrefix(String prefix) {
        List<Counselor> counselors = counselorRepository.findAll();
        int max = counselors.stream()
                .filter(c -> c.getCounselorCode() != null && c.getCounselorCode().startsWith(prefix + "-"))
                .mapToInt(c -> {
                    try {
                        String[] parts = c.getCounselorCode().split("-");
                        return Integer.parseInt(parts[1]);
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        return max + 1;
    }

    // soft delete
    @Override
    public void softDeleteCounselor(Long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek danışman bulunamadı: " + counselorId));

        // Soft-delete: @SQLDelete deleted=true
        counselorRepository.delete(counselor);

        // Audit logging
        auditLogService.logAction(
                "Counselor",
                counselorId,
                AuditActionType.DELETE,
                "system", // performedBy
                "127.0.0.1", // ipAddress
                "Danışman soft-delete ile silindi: " + counselor.getFirstName() + " " + counselor.getLastName()
        );
    }



    @Override
    public List<Counselor> getActiveCounselors() {
        return counselorRepository.findActiveCounselors();
    }

    @Override
    public List<Counselor> getAllVisibleCounselors() {
        return counselorRepository.findAllVisible();
    }

    @Override
    public Page<Counselor> getPagedActiveCounselors(Pageable pageable) {
        return counselorRepository.findAllByStatusAndDeletedFalse(CounselorStatus.ACTIVE, pageable);
    }

    @Override
    public Counselor updateCounselor(Long id, CounselorRequest request) {
        Counselor counselor = getCounselorById(id);
        counselor.setFirstName(request.getFirstName());
        counselor.setLastName(request.getLastName());
        counselor.setMiddleName(request.getMiddleName());
        counselor.setPhone(request.getPhone());
        counselor.setEmail(request.getEmail());
        counselor.setBirthDate(request.getBirthDate());
        counselor.setIsActive(Boolean.TRUE.equals(request.getIsActive()));
        counselor.setSpecialization(getSpecialtyFromString(String.valueOf(request.getSpecializationId())));
        return counselorRepository.save(counselor);
    }

    @Override
    public List<Counselor> searchCounselors(String query) {
        return counselorRepository.searchByQuery(query);
    }

    @Override
    public Map<String, Object> getSessionInfo(Long counselorId) {
        Counselor counselor = getCounselorById(counselorId);
        return Map.of(
                "sessionCount", counselor.getSessionCount(),
                "lastSessionDate", counselor.getLastSessionDate()
        );
    }

    @Override
    public Map<String, Object> getCounselorStatistics() {
        long total = counselorRepository.count();
        long active = counselorRepository.findActiveCounselors().size();
        long retired = counselorRepository.findAllVisible().stream()
                .filter(c -> c.getStatus() == CounselorStatus.RETIRED)
                .count();
        return Map.of(
                "totalCounselors", total,
                "activeCounselors", active,
                "retiredCounselors", retired
        );
    }


}
