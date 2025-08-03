package com.adilabdullayev.psychology.controller;

import com.adilabdullayev.psychology.model.Counselor;
import com.adilabdullayev.psychology.service.CounselorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counselors")
public class CounselorController {
    private final CounselorService service;

    public CounselorController(CounselorService service){
        this.service = service;
    }

    @GetMapping
    public List<Counselor> getAll(){
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<Counselor> create(@RequestBody Counselor counselor){
        return ResponseEntity.ok(service.add(counselor));
    }
}
