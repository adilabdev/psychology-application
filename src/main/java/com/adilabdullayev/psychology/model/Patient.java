package com.adilabdullayev.psychology.model;

import com.adilabdullayev.psychology.model.notes.UserCounselorNote;
import com.adilabdullayev.psychology.model.notes.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserCounselorNote> notes = new ArrayList<>();

    // Patient Code / File No
    @Column(name = "patient_code", unique = true)
    private String patientCode;

    // Patient Status (Enum)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PatientStatus status;

    // Automatic age calculating
    @Transient
    public int getAge() {
        if (this.birthDate == null) {
            return 0;
        }
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

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