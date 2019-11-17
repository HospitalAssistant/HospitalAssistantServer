package com.hospital.assistant.service;

import com.hospital.assistant.domain.NotificationEntity;
import com.hospital.assistant.model.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.EnumSet;
import java.util.Optional;

public interface NotificationService {

    Page<NotificationEntity> findAllNotAcceptedByRole(Pageable pageable, EnumSet<RoleName> roles);

    NotificationEntity save(NotificationEntity ne);

    Optional<NotificationEntity> findById(Long id);
}
