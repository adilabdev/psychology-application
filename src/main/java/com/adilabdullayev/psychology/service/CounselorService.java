package com.adilabdullayev.psychology.service;

import com.adilabdullayev.psychology.model.Counselor;
import com.adilabdullayev.psychology.repository.CounselorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CounselorService {
    private final CounselorRepository repository;

    public CounselorService(CounselorRepository repository){
        this.repository = repository;
    }

    public List<Counselor> getAll(){
        return repository.findAll();
    }

    public Counselor add(Counselor counselor){
        Optional<Counselor> existing = repository.findByEmailOrPhone(counselor.getEmail(), counselor.getPhone());
        if(existing.isPresent()){
            throw new RuntimeException("Bu e-posta veya telefonla kayıtlı danışman zaten mevcut.");
        }
        return repository.save(counselor);
    }

}
