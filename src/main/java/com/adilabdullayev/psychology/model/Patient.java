package com.adilabdullayev.psychology.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Where(clause = "deleted = false") // Soft delete filtrelemesi
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ad boş bırakılamaz.")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Soyad boş bırakılamaz.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Doğum tarihi zorunludur.")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotBlank(message = "Cinsiyet boş bırakılamaz.")
    private String gender;  // Kolon adı sorguda gender, otomatik olur

    @NotBlank(message = "Telefon numarası zorunludur.")
    private String phone;  // Kolon adı phone, otomatik olur

    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;  // Kolon adı email, otomatik olur

    @Column(name = "user_note")
    private String userNote;   // Kullanıcıdan gelen not (isteğe bağlı)

    @Column(name = "admin_note")
    private String adminNote;  // Danışmanın notu

    private Boolean deleted = false;  // deleted da otomatik

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
