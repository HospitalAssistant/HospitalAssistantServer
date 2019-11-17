package com.hospital.assistant.repository;

import com.hospital.assistant.domain.NotificationEntity;
import com.hospital.assistant.model.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.EnumSet;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT ne FROM NotificationEntity ne WHERE ne.accepted = false AND ne.intent.role.name in :roles")
    Page<NotificationEntity> findAllNotAcceptedByRole(Pageable pageable, @Param("roles") EnumSet<RoleName> roles);
}
