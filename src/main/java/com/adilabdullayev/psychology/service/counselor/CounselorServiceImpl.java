package com.adilabdullayev.psychology.service.impl;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.counselor.CounselorSpecialization;
import com.adilabdullayev.psychology.repository.counselor.CounselorRepository;
import com.adilabdullayev.psychology.service.counselor.CounselorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CounselorServiceImpl implements CounselorService {

    private final CounselorRepository counselorRepository;

    // method for bringing all patients
    @Override
    public List<Counselor> getAll() {
        return counselorRepository.findAll(); // ✅ Senin yazdığın getAll() metodunun karşılığı
    }

    // method for adding a new patient
    @Override
    public Counselor add(Counselor counselor) {
        return counselorRepository.save(counselor); // ✅ Senin yazdığın add() metodunun karşılığı
    }

    // changes counselor's speciality from string to enum
    @Override
    public CounselorSpecialization getSpecialtyFromString(String specializationStr) {
        try {
            return CounselorSpecialization.valueOf(specializationStr.toUpperCase()); // ✅ Senin yazdığın getSpecialtyFromString() metodunun karşılığı
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Geçersiz uzmanlık alanı: " + specializationStr);
        }
    }

    // Method to get a Counselor by ID
    @Override
    public Counselor getCounselorById(Long counselorId) {
        return counselorRepository.findById(counselorId)
                .orElseThrow(() -> new IllegalArgumentException("Counselor not found for id: " + counselorId)); // ✅ Senin yazdığın getCounselorById() metodunun karşılığı
    }

    @Override
    public Counselor findCounselorById(Long counselorId) {
        return counselorRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Danışman bulunamadı")); // ✅ Senin yazdığın findCounselorById() metodunun karşılığı
    }

    // dinamic filtering
    @Override
    public List<Counselor> filterCounselors(CounselorFilterRequest filterRequest) {
        return counselorRepository.filterCounselors(filterRequest); // ✅ Senin yazdığın filterCounselors() metodunun karşılığı
    }
}
