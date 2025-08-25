package com.adilabdullayev.psychology.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CounselorRequest {

    @NotBlank(message = "Ad boş bırakılamaz.")
    private String firstName;

    @NotBlank(message = "Soyad boş bırakılamaz.")
    private String lastName;

    private String middleName;

    @NotNull(message = "Doğum tarihi zorunludur.")
    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır.")
    private LocalDate birthDate;

    @NotBlank(message = "Telefon numarası zorunludur.")
    @Pattern(regexp = "^\\+\\d{10,15}$", message = "Geçerli bir telefon numarası giriniz.")
    private String phone;

    @NotBlank(message = "E-posta boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;

    private String counselorCode;
    private Boolean isActive;
    private String availableDays;
    private Long specializationId;
}
