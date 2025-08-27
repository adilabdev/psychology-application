package com.adilabdullayev.psychology.repository.archived;

import com.adilabdullayev.psychology.model.archived.ArchivedCounselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchivedCounselorRepository extends JpaRepository<ArchivedCounselor, Long> {

    // deletion process: via email or phone
    @Transactional
    @Modifying
    @Query("DELETE FROM ArchivedCounselor a WHERE a.email = :email OR a.phone = :phone")
    void deleteByEmailOrPhone(String email, String phone);

    // asset control: via email or phone
    boolean existsByEmailOrPhone(String email, String phone);

    // data retrieval: via email or phone
    Optional<ArchivedCounselor> findByEmailOrPhone(String email, String phone);

    // Optional: search via code
    Optional<ArchivedCounselor> findByCounselorCode(String counselorCode);

    // Optional: filter by specialization
    List<ArchivedCounselor> findBySpecialization(String specialization);
}
