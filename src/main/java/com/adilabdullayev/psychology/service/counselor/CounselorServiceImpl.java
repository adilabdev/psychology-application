package com.adilabdullayev.psychology.service.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
import com.adilabdullayev.psychology.dto.Response.CounselorResponse;
import com.adilabdullayev.psychology.mapper.CounselorMapper;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.AvailableDay;
import com.adilabdullayev.psychology.model.enums.AuditActionType;
import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import com.adilabdullayev.psychology.model.enums.CounselorStatus;
import com.adilabdullayev.psychology.model.archived.ArchivedCounselor;
import com.adilabdullayev.psychology.model.archived.ArchivedUserCounselorNote;
import com.adilabdullayev.psychology.repository.archived.ArchivedCounselorRepository;
import com.adilabdullayev.psychology.repository.counselor.CounselorRepository;
import com.adilabdullayev.psychology.repository.notes.ArchivedUserCounselorNoteRepository;
import com.adilabdullayev.psychology.service.audit.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CounselorServiceImpl implements CounselorService {

    private final CounselorRepository counselorRepository;
    private final AuditLogService auditLogService;
    private final CounselorMapper counselorMapper;
    private final ArchivedUserCounselorNoteRepository archivedUserCounselorNoteRepository;
    private final ArchivedCounselorRepository archivedCounselorRepository;

    public CounselorServiceImpl(CounselorRepository counselorRepository,
                                AuditLogService auditLogService,
                                CounselorMapper counselorMapper,
                                ArchivedUserCounselorNoteRepository archivedUserCounselorNoteRepository,
                                ArchivedCounselorRepository archivedCounselorRepository) {
        this.counselorRepository = counselorRepository;
        this.auditLogService = auditLogService;
        this.counselorMapper = counselorMapper;
        this.archivedUserCounselorNoteRepository = archivedUserCounselorNoteRepository;
        this.archivedCounselorRepository = archivedCounselorRepository;
    }

    // method for bringing all counselors
    @Override
    public List<Counselor> getAll() {
        return counselorRepository.findAll();
    }

    // method for adding a new counselor
    @Override
    public Counselor add(CounselorRequest request) {
        return addCounselor(request, "SYSTEM", null);
    }

    @Override
    @Transactional
    public Counselor addCounselor(CounselorRequest request, String performedBy, HttpServletRequest httpRequest) {
        String email = request.getEmail();
        String phone = request.getPhone();
        String ipAddress = httpRequest != null ? httpRequest.getRemoteAddr() : "SYSTEM";

        Optional<Counselor> existingOpt = counselorRepository.findByEmailOrPhone(email, phone);
        existingOpt.ifPresent(counselorRepository::delete);

        archivedCounselorRepository.findByEmailOrPhone(email, phone)
                .ifPresent(archivedCounselorRepository::delete);

        Counselor counselor = new Counselor();
        counselor.setFirstName(request.getFirstName());
        counselor.setLastName(request.getLastName());
        counselor.setMiddleName(request.getMiddleName());
        counselor.setBirthDate(request.getBirthDate());
        counselor.setPhone(phone);
        counselor.setEmail(email);
        counselor.setIsActive(Boolean.TRUE.equals(request.getIsActive()));

        String code = request.getCounselorCode();
        if (code == null || code.isBlank()) {
            code = "CS-" + String.format("%04d", getNextSequenceForPrefix("CS"));
        }
        counselor.setCounselorCode(code);

        counselor.setSpecialization(getSpecialtyFromString(request.getSpecialization()));

        if (request.getAvailableDays() != null) {
            List<AvailableDay> availableDays = request.getAvailableDays().stream()
                    .map(day -> AvailableDay.valueOf(day.toUpperCase()))
                    .collect(Collectors.toList());
            counselor.setAvailableDays(availableDays);
        }

        Counselor saved = counselorRepository.save(counselor);

        auditLogService.logAction(
                AuditActionType.CREATE,
                "Counselor",
                saved.getId(),
                performedBy,
                ipAddress,
                "Yeni danışman eklendi: " + saved.getFirstName() + " " + saved.getLastName()
        );

        return saved;
    }

    // changes counselor's speciality from string to enum
    @Override
    public CounselorSpecialization getSpecialtyFromString(String specializationStr) {
        try {
            return CounselorSpecialization.valueOf(specializationStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Geçersiz uzmanlık alanı: " + specializationStr);
        }
    }

    // Method to get a Counselor by ID
    @Override
    public CounselorResponse findCounselorById(Long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));
        return counselorMapper.toResponse(counselor);
    }

    // dynamic filtering
    @Override
    public List<Counselor> filterCounselors(CounselorFilterRequest filterRequest) {
        return counselorRepository.filterCounselors(filterRequest);
    }

    @Override
    public Optional<Counselor> findByEmailOrPhone(String email, String phone) {
        return counselorRepository.findByEmailOrPhone(email, phone);
    }

    @Override
    public Integer getNextSequenceForPrefix(String prefix) {
        return counselorRepository.findAll().stream()
                .filter(c -> c.getCounselorCode() != null && c.getCounselorCode().startsWith(prefix + "-"))
                .mapToInt(c -> {
                    try {
                        return Integer.parseInt(c.getCounselorCode().split("-")[1]);
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0) + 1;
    }

    // soft delete
    @Transactional
    @Override
    public void softDeleteCounselor(Long counselorId, String deletionReason, String deletedBy, HttpServletRequest request) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek danışman bulunamadı: " + counselorId));

        String ipAddress = request.getRemoteAddr();

        ArchivedCounselor archived = new ArchivedCounselor();
        BeanUtils.copyProperties(counselor, archived, "id", "createdAt", "updatedAt");

        archived.setCreatedAt(counselor.getCreatedAt());
        archived.setUpdatedAt(counselor.getUpdatedAt());
        archived.setDeleted(true);
        archived.setDeletedAt(LocalDateTime.now());
        archived.setDeletedBy(deletedBy);
        archived.setIpAddress(ipAddress);

        archivedCounselorRepository.save(archived);

        if (counselor.getCounselorNotes() != null) {
            List<ArchivedUserCounselorNote> archivedNotes = counselor.getCounselorNotes().stream().map(note -> {
                ArchivedUserCounselorNote an = new ArchivedUserCounselorNote();
                BeanUtils.copyProperties(note, an, "id", "createdAt", "updatedAt");
                an.setDeletedAt(LocalDateTime.now());
                return an;
            }).collect(Collectors.toList());

            archivedUserCounselorNoteRepository.saveAll(archivedNotes);
            counselor.getCounselorNotes().clear();
        }

        counselorRepository.delete(counselor);

        auditLogService.logAction(
                AuditActionType.ARCHIVE,
                "Counselor",
                counselorId,
                deletedBy,
                ipAddress,
                "Danışman arşivlendi. Sebep: " + deletionReason
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
    @Transactional
    public CounselorResponse updateCounselor(Long id, CounselorRequest request, String performedBy, HttpServletRequest httpRequest) {
        Counselor counselor = counselorRepository.findById(id)
                .filter(c -> !c.getDeleted())
                .orElseThrow(() -> new RuntimeException("Danışman bulunamadı veya silinmiş. ID: " + id));

        Optional<Counselor> conflictOpt = counselorRepository.findByEmailOrPhone(request.getEmail(), request.getPhone());
        if (conflictOpt.isPresent() && !conflictOpt.get().getId().equals(id)) {
            throw new RuntimeException("Bu e-posta veya telefon başka bir danışmana ait.");
        }

        counselor.setFirstName(request.getFirstName());
        counselor.setLastName(request.getLastName());
        counselor.setMiddleName(request.getMiddleName());
        counselor.setPhone(request.getPhone());
        counselor.setEmail(request.getEmail());
        counselor.setBirthDate(request.getBirthDate());
        counselor.setIsActive(Boolean.TRUE.equals(request.getIsActive()));
        counselor.setSpecialization(getSpecialtyFromString(request.getSpecialization()));

        Counselor updated = counselorRepository.save(counselor);

        auditLogService.logAction(
                AuditActionType.UPDATE,
                "Counselor",
                updated.getId(),
                performedBy,
                httpRequest != null ? httpRequest.getRemoteAddr() : "SYSTEM",
                "Danışman bilgileri güncellendi: " + updated.getFirstName() + " " + updated.getLastName()
        );

        return counselorMapper.toResponse(updated);
    }

    @Override
    public List<Counselor> searchCounselors(String query) {
        return counselorRepository.searchByQuery(query);
    }

    @Override
    public Map<String, Object> getSessionInfo(Long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));
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
