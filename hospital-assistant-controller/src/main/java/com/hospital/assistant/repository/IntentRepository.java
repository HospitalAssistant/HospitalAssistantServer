package com.hospital.assistant.repository;

import com.hospital.assistant.domain.IntentEntity;
import com.hospital.assistant.model.IntentName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntentRepository extends JpaRepository<IntentEntity, Long> {

    Optional<IntentEntity> findByName(IntentName intentName);
}
