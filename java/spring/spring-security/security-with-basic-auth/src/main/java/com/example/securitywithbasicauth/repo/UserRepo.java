package com.example.securitywithbasicauth.repo;

import com.example.securitywithbasicauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    @Query("""
        SELECT u FROM User u WHERE u.email = :email
        """)
    Optional<User> findByUsername(String email);
}