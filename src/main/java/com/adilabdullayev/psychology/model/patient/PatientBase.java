package com.adilabdullayev.psychology.model.patient;

import com.adilabdullayev.psychology.model.BaseEntity;
import com.adilabdullayev.psychology.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "patient_base")
@Data
public abstract class PatientBase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // name - must
    @NotBlank(message = "Ad boş bırakılamaz.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // lastname, must
    @NotBlank(message = "Soyad boş bırakılamaz.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    // birth date - patient's age 'll be calculate, must
    @NotNull(message = "Doğum tarihi zorunludur.")
    @Column(name = "birth_date", nullable = false)
    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır.")
    private LocalDate birthDate;

    // gender - must
    @NotNull(message = "Cinsiyet boş bırakılamaz.")
    @Column(nullable = false)
    private Gender gender;

    // phone number - must
    @NotBlank(message = "Telefon numarası zorunludur.")
    @Pattern(regexp = "^\\+\\d{10,15}$", message = "Geçerli bir telefon numarası giriniz.")
    @Column(nullable = false)
    private String phone;


    // e-post  - in validated format, must
    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    @Column(nullable = false)
    private String email;

    // ip address
    @Column(name = "ip_address")
    private String ipAddress;


    // for getting automatic age calculation and not saved (transient)
    @Transient
    public int getAge() {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Getters and Setters
}