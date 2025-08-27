package com.adilabdullayev.psychology.repository.archived;

import com.adilabdullayev.psychology.model.archived.ArchivedPatients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface ArchivedPatientRepository extends JpaRepository<ArchivedPatients, Long> {

    // deletes client by stade email or phone number
    @Transactional
    @Modifying
    @Query("DELETE FROM ArchivedPatients a WHERE a.email = :email OR a.phone = :phone")
    void deleteByEmailOrPhone(String email, String phone);

    // checks if there is a record by specific email address or phone number
    boolean existsByEmailOrPhone(String email, String phone);

    // returns client informations by specific email address or phone number
    Optional<ArchivedPatients> findByEmailOrPhone(String email, String phone);

}