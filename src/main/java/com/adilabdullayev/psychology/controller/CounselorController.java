package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Counselor> create(@RequestBody Counselor counselor) {
        return ResponseEntity.ok(counselorService.add(counselor));
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Counselor>> filterCounselors(@RequestBody CounselorFilterRequest filterRequest) {
        List<Counselor> result = counselorService.filterCounselors(filterRequest);
        return ResponseEntity.ok(result);
    }
}

