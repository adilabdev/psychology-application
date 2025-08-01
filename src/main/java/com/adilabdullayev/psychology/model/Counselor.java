package com.adilabdullayev.psychology.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="counselors", uniqueConstraints={
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ad boş olamaz.")
    private String firstName;

    @NotBlank(message = "Soyisim boş olamaz.")
    private String lastName;

    @Email
    @NotBlank(message = "E-posta zorunludur.")
    private String email;

    @NotBlank(message = "Telefon numarası zorunludur.")
    private String phone;

    private String speciality;

    private String availableDays;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

}
