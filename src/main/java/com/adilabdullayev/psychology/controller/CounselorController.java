package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.dto.Response.CounselorResponse;
import com.adilabdullayev.psychology.mapper.CounselorMapper;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/counselors")
@RequiredArgsConstructor
public class CounselorController {

    private final CounselorService counselorService;
    private final CounselorMapper counselorMapper;

    @GetMapping
    public ResponseEntity<List<CounselorResponse>> getAll() {
        List<Counselor> counselors = counselorService.getAll();
        return ResponseEntity.ok(counselorMapper.toResponseList(counselors));
    }

    @PostMapping
    public ResponseEntity<CounselorResponse> createCounselor(
            @Valid @RequestBody CounselorRequest request,
            @RequestParam(defaultValue = "system") String performedBy,
            HttpServletRequest httpRequest
    ) {
        Counselor saved = counselorService.addCounselor(request, performedBy, httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(counselorMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteCounselor(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Sebep belirtilmedi") String reason,
            @RequestParam(defaultValue = "Sistem") String deletedBy,
            HttpServletRequest request
    ) {
        counselorService.softDeleteCounselor(id, reason, deletedBy, request);
        return ResponseEntity.ok("Danışman başarıyla arşivlendi.");
    }

    @GetMapping("/active")
    public ResponseEntity<List<CounselorResponse>> getActiveCounselors() {
        List<Counselor> active = counselorService.getActiveCounselors();
        return ResponseEntity.ok(counselorMapper.toResponseList(active));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CounselorResponse>> getAllVisibleCounselors() {
        return ResponseEntity.ok(counselorMapper.toResponseList(counselorService.getAllVisibleCounselors()));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CounselorResponse>> getPagedActiveCounselors(Pageable pageable) {
        Page<Counselor> paged = counselorService.getPagedActiveCounselors(pageable);
        Page<CounselorResponse> response = paged.map(counselorMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CounselorResponse>> searchCounselors(@RequestParam String query) {
        return ResponseEntity.ok(counselorMapper.toResponseList(counselorService.searchCounselors(query)));
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<Map<String, Object>> getSessionInfo(@PathVariable Long id) {
        return ResponseEntity.ok(counselorService.getSessionInfo(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCounselorStats() {
        return ResponseEntity.ok(counselorService.getCounselorStatistics());
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterCounselors(@Valid @RequestBody CounselorFilterRequest filterRequest) {

        boolean hasValidField =
                (filterRequest.getFirstName() != null && !filterRequest.getFirstName().isBlank()) ||
                        (filterRequest.getLastName() != null && !filterRequest.getLastName().isBlank()) ||
                        (filterRequest.getPhone() != null && !filterRequest.getPhone().isBlank()) ||
                        (filterRequest.getEmail() != null && !filterRequest.getEmail().isBlank()) ||
                        (filterRequest.getBirthYear() != null) ||
                        (filterRequest.getBirthDate() != null) ||
                        (filterRequest.getCreatedAfter() != null) ||
                        (filterRequest.getUpdatedBefore() != null) ||
                        (filterRequest.getSpecialization() != null && !filterRequest.getSpecialization().isBlank()) ||
                        (filterRequest.getRole() != null && !filterRequest.getRole().isBlank()) ||
                        (filterRequest.getIsActive() != null);

        if (!hasValidField) {
            return ResponseEntity.badRequest().body("En az bir geçerli filtre alanı girilmelidir.");
        }

        List<Counselor> filtered = counselorService.filterCounselors(filterRequest);
        return ResponseEntity.ok(counselorMapper.toResponseList(filtered));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CounselorResponse> updateCounselor(
            @PathVariable Long id,
            @Valid @RequestBody CounselorRequest request,
            @RequestParam(defaultValue = "system") String performedBy,
            HttpServletRequest httpRequest
    ) {
        CounselorResponse updated = counselorService.updateCounselor(id, request, performedBy, httpRequest);
        return ResponseEntity.ok(updated);
    }

}


