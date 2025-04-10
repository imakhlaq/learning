package com.sharefile.securedoc.repository;

import com.sharefile.securedoc.entity.UserCredentialEntity;
import com.sharefile.securedoc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntitiesByUserId(String userId);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
}