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
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Entity
@Setter
@Getter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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

        var NewClient = new Client();

        NewClient.setClientId(registeredClient.getClientId());
        NewClient.setClientSecret(registeredClient.getClientSecret());

        NewClient.setRedirectUri(
            registeredClient
                .getRedirectUris()
                .stream()
                .findFirst().orElse(null)
        );

        NewClient.setScope(
            registeredClient
                .getScopes()
                .stream()
                .findFirst().orElse(null)
        );

        NewClient.setAuthMethod(
            registeredClient
                .getClientAuthenticationMethods()
                .stream()
                .findFirst()
                .orElse(null)
                .getValue()
        );

        NewClient.setGrantType(
            registeredClient
                .getAuthorizationGrantTypes()
                .stream()
                .findFirst()
                .orElse(null)
                .getValue()
        );

        return NewClient;
    }

    public static RegisteredClient from(Client client) {

        return RegisteredClient
            .withId(client.getId())
            .clientId(client.getClientId())
            .clientSecret(client.getClientSecret())
            .redirectUri(client.getRedirectUri())
            .scope(client.getScope())
            .clientAuthenticationMethod(new ClientAuthenticationMethod(client.getAuthMethod()))
            .authorizationGrantType(new AuthorizationGrantType(client.getGrantType()))
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)//to have refresh token
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)//if client is another service
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(5000))
                .build())
            .build();
    }
}