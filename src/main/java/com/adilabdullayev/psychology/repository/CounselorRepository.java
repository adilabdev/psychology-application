package com.adilabdullayev.psychology.repository;

import com.adilabdullayev.psychology.model.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    Optional<Counselor> findByEmailOrPhone(String email, String phone);
}
