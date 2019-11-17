package com.hospital.assistant.service;

import com.hospital.assistant.domain.RoleEntity;
import com.hospital.assistant.model.RoleName;
import com.hospital.assistant.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<RoleEntity> findByName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }
}
