package com.akhlaq.securenote.repositories;

import com.akhlaq.securenote.models.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NotBlank String username);
    Boolean existsByUsername(@NotBlank String username);
}