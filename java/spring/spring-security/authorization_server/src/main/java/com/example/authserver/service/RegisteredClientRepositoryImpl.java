package com.example.authserver.service;

import com.example.authserver.entity.Client;
import com.example.authserver.repo.IClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@AllArgsConstructor
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private final IClientRepo clientRepo;

    @Override
    public void save(RegisteredClient registeredClient) {

        clientRepo.save(Client.from(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {

        var client = clientRepo.findById(Long.valueOf(id))
            .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        return Client.from(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {

        var client = clientRepo.findClientsByClientId(clientId)
            .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        return Client.from(client);
    }
}