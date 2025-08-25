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

import java.util.List;

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

}

