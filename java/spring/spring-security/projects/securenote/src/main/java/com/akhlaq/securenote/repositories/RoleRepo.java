package com.akhlaq.securenote.repositories;

import com.akhlaq.securenote.models.AppRoles;
import com.akhlaq.securenote.models.Role;
import com.akhlaq.securenote.models.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRoles roleName);
}