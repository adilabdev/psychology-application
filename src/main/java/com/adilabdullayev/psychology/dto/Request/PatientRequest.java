package com.adilabdullayev.psychology.dto.Request;

import com.adilabdullayev.psychology.model.enums.Gender;
import com.adilabdullayev.psychology.model.enums.PatientStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PatientRequest {

    @NotBlank(message = "Ad boş bırakılamaz.")
    private String firstName;

    @NotBlank(message = "Soyad boş bırakılamaz.")
    private String lastName;

    private String middleName;

    @NotNull(message = "Doğum tarihi zorunludur.")
    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır.")
    private LocalDate birthDate;

    @NotNull(message = "Cinsiyet boş bırakılamaz.")
    private Gender gender;

    @NotBlank(message = "Telefon numarası zorunludur.")
    @Pattern(regexp = "^\\+\\d{10,15}$", message = "Geçerli bir telefon numarası giriniz.")
    private String phone;

    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;

    private PatientStatus status;

    private List<Long> noteIds; // if there is notes assign it with id
}
