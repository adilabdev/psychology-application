package com.adilabdullayev.psychology.PatientTests;

import org.junit.jupiter.api.Test;

public class PatientPerformanceTests {

    @Test
    public void testPerformance() {
        long start = System.currentTimeMillis();

        // Çok sayıda hasta ekle ve ölç
        long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start) + "ms");
    }
}
