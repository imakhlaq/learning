package com.example.authserver.repo;

import com.example.authserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}