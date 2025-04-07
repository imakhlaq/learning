package com.example.authserver.repo;

import com.example.authserver.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IClientRepo extends JpaRepository<Client, Long> {

    Optional<Client> findClientsByClientId(String clientId);
}