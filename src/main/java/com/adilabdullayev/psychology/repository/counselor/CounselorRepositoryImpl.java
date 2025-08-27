package com.adilabdullayev.psychology.repository.counselor;

import com.adilabdullayev.psychology.dto.Request.CounselorFilterRequest;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CounselorRepositoryImpl implements CounselorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Counselor> filterCounselors(CounselorFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Counselor> query = cb.createQuery(Counselor.class);
        Root<Counselor> root = query.from(Counselor.class);

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

        // Extras (e.g. specialization, role, isActive)
        if (filter.getSpecialization() != null && !filter.getSpecialization().isBlank()) {
            try {
                CounselorSpecialization specEnum = CounselorSpecialization.valueOf(filter.getSpecialization().toUpperCase());
                predicates.add(cb.equal(root.get("specialization"), specEnum));
            } catch (IllegalArgumentException e) {
                // invalid specialization value, ignore filter
            }
        }


        if (filter.getRole() != null && !filter.getRole().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("role")), filter.getRole().toLowerCase()));
        }

        if (filter.getIsActive() != null) {
            predicates.add(cb.equal(root.get("isActive"), filter.getIsActive()));
        }

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Counselor> typedQuery = entityManager.createQuery(query);

        return typedQuery.getResultList();
    }
}
