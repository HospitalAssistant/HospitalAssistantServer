package com.hospital.assistant.service;

import com.hospital.assistant.domain.IntentEntity;
import com.hospital.assistant.model.IntentName;

import java.util.List;
import java.util.Optional;

public interface IntentService {

    List<IntentEntity> findAll();

    Optional<IntentEntity> findByName(IntentName intentName);
}
