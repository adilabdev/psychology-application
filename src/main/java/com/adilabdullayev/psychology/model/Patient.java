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
@Where(clause = "deleted = false") // Soft delete filtering
@Table(name = "patients")
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
    private String gender;

    @NotBlank(message = "Telefon numarası zorunludur.")
    private String phone;

    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;

    @Column(name = "user_note")
    private String userNote;

    @Column(name = "admin_note")
    private String adminNote;

    private Boolean deleted = false;

    public Boolean getDeleted() {
        return deleted;
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
