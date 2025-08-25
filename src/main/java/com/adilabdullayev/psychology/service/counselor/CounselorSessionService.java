package com.adilabdullayev.psychology.service.counselor;

import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.repository.counselor.CounselorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CounselorSessionService {

    private final CounselorRepository counselorRepository;

    public CounselorSessionService(CounselorRepository counselorRepository) {
        this.counselorRepository = counselorRepository;
    }

    public void recordSession(Long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found"));

        counselor.setSessionCount((counselor.getSessionCount() == null ? 0 : counselor.getSessionCount()) + 1);
        counselor.setLastSessionDate(LocalDateTime.now());

        counselorRepository.save(counselor);
    }
}
