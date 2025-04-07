package com.sharefile.securedoc.repository;

import com.sharefile.securedoc.entity.ConfirmationEntity;
import com.sharefile.securedoc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationRepo extends JpaRepository<ConfirmationEntity, Long> {
    Optional<ConfirmationEntity> findAllByKey(String key);
    Optional<ConfirmationEntity> findByUser(UserEntity user);
}