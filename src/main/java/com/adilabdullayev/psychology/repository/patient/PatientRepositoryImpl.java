package com.adilabdullayev.psychology.repository.patient;

import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import com.adilabdullayev.psychology.model.patient.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Patient> searchPatients(String keyword) {
        // create criteria builder and query for Patient entity
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        // search by firstName, lastName, email, or phone
        Predicate predicate = cb.or(
                cb.like(cb.lower(root.get("firstName")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("lastName")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("phone")), "%" + keyword.toLowerCase() + "%")
        );

        // apply predicate and exclude deleted patients
        query.where(cb.and(predicate, cb.isFalse(root.get("deleted"))));

        // execute query and return results
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Patient> filterPatients(PatientFilterRequest filter) {
        // create criteria builder and query for Patient entity
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        List<Predicate> predicates = new ArrayList<>();

        // filter by firstName if provided
        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
        }

        // filter by lastName if provided
        if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
        }

        // filter by phone if provided
        if (filter.getPhone() != null && !filter.getPhone().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("phone")), "%" + filter.getPhone().toLowerCase() + "%"));
        }

        // filter by email if provided
        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        // filter by gender if provided
        if (filter.getGender() != null && !filter.getGender().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("gender")), filter.getGender().toLowerCase()));
        }

        // filter by birth year if provided
        if (filter.getBirthYear() != null) {
            predicates.add(cb.equal(cb.function("year", Integer.class, root.get("birthDate")), filter.getBirthYear()));
        }

        // filter by exact birth date if provided
        if (filter.getBirthDate() != null) {
            predicates.add(cb.equal(root.get("birthDate"), filter.getBirthDate()));
        }

        // filter by createdAfter date if provided
        if (filter.getCreatedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAfter().atStartOfDay()));
        }

        // filter by updatedBefore date if provided
        if (filter.getUpdatedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), filter.getUpdatedBefore().atTime(23, 59, 59)));
        }

        // exclude deleted patients
        predicates.add(cb.isFalse(root.get("deleted")));

        // apply all predicates
        query.where(predicates.toArray(new Predicate[0]));

        // execute query and return results
        TypedQuery<Patient> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}

// TODO: Refactor generateUniquePatientCode to use transactional sequence logic
// Similar to getNextSequenceForPrefix("CS") used in CounselorService


//1. Optional<Patient> findByEmail(String email)
//2. Optional<Patient> findByPhone(String phone)
//3. Optional<Patient> findByEmailOrPhoneAndDeletedFalse(String email, String phone)
//4. Page<Patient> findByEmailContainingIgnoreCaseAndFirstNameContainingIgnoreCase(...)
//5. Page<Patient> findAllByDeletedFalse(Pageable pageable)
//📍 Kullanılabilecek yerler:
//✅ PatientServiceImpl.java
//Bu metotlar genellikle servis katmanında çağrılır. Örnek:
//
//java
//@Override
//public Optional<Patient> findByEmailOrPhone(String email, String phone) {
//    return patientRepository.findByEmailOrPhoneAndDeletedFalse(email, phone);
//}
//✅ PatientController.java
//Controller’da filtreleme, arama, detay getirme gibi işlemlerde kullanılabilir:
//
//java
//@GetMapping("/search")
//public ResponseEntity<?> searchPatient(@RequestParam String email, @RequestParam String phone) {
//    Optional<Patient> patient = patientService.findByEmailOrPhone(email, phone);
//    return patient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//}
//✅ PatientService.java → Arayüzde tanımlanmalı
//java
//Optional<Patient> findByEmailOrPhone(String email, String phone);
//Page<Patient> getActivePatients(Pageable pageable);
//✅ PatientServiceImpl.java → Uygulama
//java
//@Override
//public Page<Patient> getActivePatients(Pageable pageable) {
//    return patientRepository.findAllByDeletedFalse(pageable);
//}