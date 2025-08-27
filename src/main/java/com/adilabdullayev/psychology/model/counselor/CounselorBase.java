package com.adilabdullayev.psychology.model.counselor;

import com.adilabdullayev.psychology.model.BaseEntity;
import com.adilabdullayev.psychology.model.enums.AvailableDay;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.SQLDelete;


import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "counselor_base")
@SQLDelete(sql = "UPDATE counselor_base SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false") // for soft-delete
public abstract class CounselorBase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name, can't be blank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // Lastname, can't be blank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Middlename, optional
    @Column(name = "middle_name")
    private String middleName;

    // Birthdate, can't be null and must be in the past
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    // Gender, optional but should be validated
    @Column(nullable = false)
    private String phone;

    // Email, can't be blank and should be valid
    @Column(nullable = false)
    private String email;

    @ElementCollection(targetClass = AvailableDay.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "counselor_available_days", joinColumns = @JoinColumn(name = "counselor_id"))
    @Column(name = "day")
    private List<AvailableDay> availableDays;
}
