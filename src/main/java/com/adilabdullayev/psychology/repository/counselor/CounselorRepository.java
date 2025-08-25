package com.adilabdullayev.psychology.repository.counselor;

import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.AvailableDay;
import com.adilabdullayev.psychology.model.enums.CounselorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long>, CounselorRepositoryCustom {

    Optional<Counselor> findByEmail(String email);

    Optional<Counselor> findByPhone(String phone);

    @Query("SELECT c FROM Counselor c WHERE c.email = :email OR c.phone = :phone")
    Optional<Counselor> findByEmailOrPhone(@Param("email") String email, @Param("phone") String phone);

    Optional<Counselor> findByIdAndDeletedFalse(Long id);

    Page<Counselor> findAllByDeletedFalse(Pageable pageable);

    List<Counselor> findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String email, String firstName);

    Page<Counselor> findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String email, String firstName, Pageable pageable);

    @Query("SELECT c FROM Counselor c JOIN c.availableDays d WHERE d = :day")
    List<Counselor> findByAvailableDay(@Param("day") AvailableDay day);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Counselor c WHERE c.counselorCode = :code")
    boolean existsByCounselorCode(@Param("code") String counselorCode);

    @Query("SELECT c FROM Counselor c WHERE c.status = 'ACTIVE' AND c.deleted = false")
    List<Counselor> findActiveCounselors();

    @Query("SELECT c FROM Counselor c WHERE c.deleted = false")
    List<Counselor> findAllVisible();

    Page<Counselor> findAllByStatusAndDeletedFalse(CounselorStatus status, Pageable pageable);

    @Query("SELECT c FROM Counselor c WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Counselor> searchByQuery(@Param("query") String query);

}
