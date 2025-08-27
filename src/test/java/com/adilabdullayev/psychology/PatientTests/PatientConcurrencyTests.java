package com.adilabdullayev.psychology.PatientTests;

import com.adilabdullayev.psychology.service.patient.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class PatientConcurrencyTests {

    @Autowired
    private PatientService patientService;

    @Test
    public void testConcurrentUpdate() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task = () -> {
            // patientService.updatePatient(...) çağrısı
        };

        executor.submit(task);
        executor.submit(task);

        executor.shutdown();
        executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);

        // assertion jobs
    }
}
