package com.hospital.assistant.service;

import com.hospital.assistant.domain.RoleEntity;
import com.hospital.assistant.model.RoleName;

import java.util.Optional;

public interface RoleService {

    Optional<RoleEntity> findByName(RoleName roleName);
}
