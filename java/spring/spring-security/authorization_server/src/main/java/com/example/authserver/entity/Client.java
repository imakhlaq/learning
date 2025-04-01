package com.example.authserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Optional;

@Entity
@Setter
@Getter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String clientId;
    private String clientSecret;

    //these are many-to-many relationship
    private String scope;
    private String authMethod;
    private String grantType;

    //these are one-to-many relationship
    private String redirectUri;

    //convert RegisteredClient to client
    public static Client from(RegisteredClient registeredClient) {

        var client = new Client();

        client.setClientId(registeredClient.getClientId());
        client.setClientSecret(registeredClient.getClientSecret());

        client.setRedirectUri(
            registeredClient
                .getRedirectUris()
                .stream()
                .findFirst().orElse(null)
        );

        client.setScope(
            registeredClient
                .getScopes()
                .stream()
                .findFirst().orElse(null)
        );

        client.setAuthMethod(
            registeredClient
                .getClientAuthenticationMethods()
                .stream()
                .findFirst()
                .orElse(null)
                .getValue()
        );

        client.setGrantType(
            registeredClient
                .getAuthorizationGrantTypes()
                .stream()
                .findFirst()
                .orElse(null)
                .getValue()
        );

        return client;
    }

    public static RegisteredClient from(Client client) {

        return RegisteredClient
            .withId(String.valueOf(client.getId()))
            .clientId(client.getClientId())
            .clientSecret(client.getClientSecret())
            .redirectUri(client.getRedirectUri())
            .scope(client.getScope())
            .clientAuthenticationMethod(new ClientAuthenticationMethod(client.getAuthMethod()))
            .authorizationGrantType(new AuthorizationGrantType(client.getGrantType()))
            .build();
    }
}