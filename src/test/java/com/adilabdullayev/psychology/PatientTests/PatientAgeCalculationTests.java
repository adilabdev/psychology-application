package com.adilabdullayev.psychology.PatientTests;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


class PatientAgeCalculationTests {

    @Test
    void shouldCalculateCorrectAge() {
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        int currentYear = 2025;
        int expectedAge = 35;

        int calculatedAge = currentYear - birthDate.getYear();
        assertEquals(expectedAge, calculatedAge);
    }

    @Test
    void shouldReturnZeroForTodayBirth() {
        LocalDate birthDate = LocalDate.now();
        int expectedAge = 0;

        int calculatedAge = LocalDate.now().getYear() - birthDate.getYear();
        assertEquals(expectedAge, calculatedAge);
    }

    @Test
    void shouldThrowForFutureBirthDate() {
        LocalDate futureBirthDate = LocalDate.now().plusYears(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validateBirthDate(futureBirthDate);
        });

        String expectedMessage = "Doğum tarihi bugünden sonra olamaz";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Doğum tarihi bugünden sonra olamaz");
        }
    }
}
