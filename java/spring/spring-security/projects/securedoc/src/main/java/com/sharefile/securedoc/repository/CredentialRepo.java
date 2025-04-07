package com.sharefile.securedoc.repository;

import com.sharefile.securedoc.entity.UserCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepo extends JpaRepository<UserCredentialEntity, Long> {
    Optional<UserCredentialEntity> getUserCredentialEntityByUser_Id(Long userId);
}