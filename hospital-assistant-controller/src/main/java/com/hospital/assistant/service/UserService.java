package com.hospital.assistant.service;

import com.hospital.assistant.domain.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findById(Long id);

    List<UserEntity> findAll();
}
