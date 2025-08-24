package com.adilabdullayev.psychology.model.counselor;

import com.adilabdullayev.psychology.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.SQLDelete;


import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "counselor_base")
@Data
@SQLDelete(sql = "UPDATE counselor_base SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false") // for soft-delete
public abstract class CounselorBase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name, can't be blank
    @NotBlank(message = "Ad boş bırakılamaz.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // Lastname, can't be blank
    @NotBlank(message = "Soyad boş bırakılamaz.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Middlename, optional
    @Column(name = "middle_name")
    private String middleName;

    // Birthdate, can't be null and must be in the past
    @NotNull(message = "Doğum tarihi zorunludur.")
    @Column(name = "birth_date", nullable = false)
    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır.")
    private LocalDate birthDate;

    // Gender, optional but should be validated
    @NotBlank(message = "Telefon numarası zorunludur.")
    @Pattern(regexp = "^\\+\\d{10,15}$", message = "Geçerli bir telefon numarası giriniz.")
    @Column(nullable = false)
    private String phone;

    // Email, can't be blank and should be valid
    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    @Column(nullable = false)
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
