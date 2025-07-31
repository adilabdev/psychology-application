package com.adilabdullayev.psychology.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ad boş bırakılamaz.")
    private String firstname;

    @NotBlank(message = "Soyad boş bırakılamaz.")
    private String lastName;

    @NotNull(message = "Doğum tarihi zorunludur.")
    private LocalDate birthDate;

    @NotBlank(message = "Cinsiyet boş bırakılamaz.")
    private String gender;

    @NotBlank(message = "Telefon numarası zorunludur.")
    private String phone;

    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;

    private String userNote;
    private String adminNote;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
