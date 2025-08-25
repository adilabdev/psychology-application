package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
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

    @GetMapping
    public List<Counselor> getAll() {
        return counselorService.getAll();
    }

    @PostMapping
    public ResponseEntity<Counselor> createCounselor(@Valid @RequestBody CounselorRequest request) {
        Counselor saved = counselorService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Counselor>> filterCounselors(@RequestBody CounselorFilterRequest filterRequest) {
        List<Counselor> result = counselorService.filterCounselors(filterRequest);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCounselor(@PathVariable Long id) {
        counselorService.softDeleteCounselor(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/active")
    public ResponseEntity<List<Counselor>> getActiveCounselors() {
        return ResponseEntity.ok(counselorService.getActiveCounselors());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Counselor>> getAllVisibleCounselors() {
        return ResponseEntity.ok(counselorService.getAllVisibleCounselors());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Counselor>> getPagedActiveCounselors(Pageable pageable) {
        return ResponseEntity.ok(counselorService.getPagedActiveCounselors(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Counselor> updateCounselor(@PathVariable Long id, @Valid @RequestBody CounselorRequest request) {
        return ResponseEntity.ok(counselorService.updateCounselor(id, request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Counselor>> searchCounselors(@RequestParam String query) {
        return ResponseEntity.ok(counselorService.searchCounselors(query));
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<Map<String, Object>> getSessionInfo(@PathVariable Long id) {
        return ResponseEntity.ok(counselorService.getSessionInfo(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCounselorStats() {
        return ResponseEntity.ok(counselorService.getCounselorStatistics());
    }


}

