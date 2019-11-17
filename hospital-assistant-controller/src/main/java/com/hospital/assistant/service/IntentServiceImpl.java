package com.hospital.assistant.service;

import com.hospital.assistant.domain.IntentEntity;
import com.hospital.assistant.model.IntentName;
import com.hospital.assistant.repository.IntentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("intentService")
public class IntentServiceImpl implements IntentService {

    @Autowired
    private IntentRepository intentRepository;

    @Override
    public List<IntentEntity> findAll() {
        return intentRepository.findAll();
    }

    public Optional<IntentEntity> findByName(IntentName intentName) {
        return intentRepository.findByName(intentName);
    }
}
