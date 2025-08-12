package com.adilabdullayev.psychology.repository.patient;

import com.adilabdullayev.psychology.dto.Request.PatientFilterRequest;
import com.adilabdullayev.psychology.model.patient.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Patient> filterPatients(PatientFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Patient> query = cb.createQuery(Patient.class);
        Root<Patient> root = query.from(Patient.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
        }

        if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
        }

        if (filter.getPhone() != null && !filter.getPhone().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("phone")), "%" + filter.getPhone().toLowerCase() + "%"));
        }

        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }


        if (filter.getGender() != null && !filter.getGender().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("gender")), filter.getGender().toLowerCase()));
        }

        if (filter.getBirthYear() != null) {
            predicates.add(cb.equal(cb.function("year", Integer.class, root.get("birthDate")), filter.getBirthYear()));
        }

        if (filter.getBirthDate() != null) {
            predicates.add(cb.equal(root.get("birthDate"), filter.getBirthDate()));
        }

        if (filter.getCreatedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAfter().atStartOfDay()));
        }

        if (filter.getUpdatedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), filter.getUpdatedBefore().atTime(23, 59, 59)));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
