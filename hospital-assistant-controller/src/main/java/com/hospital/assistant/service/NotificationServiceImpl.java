package com.hospital.assistant.service;

import com.hospital.assistant.domain.NotificationEntity;
import com.hospital.assistant.model.RoleName;
import com.hospital.assistant.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Page<NotificationEntity> findAllNotAcceptedByRole(Pageable pageable, EnumSet<RoleName> roles) {
        return notificationRepository.findAllNotAcceptedByRole(pageable, roles);
    }

    @Override
    public NotificationEntity save(NotificationEntity ne) {
        return notificationRepository.save(ne);
    }

    public Optional<NotificationEntity> findById(Long id) {
        return notificationRepository.findById(id);
    }
}
