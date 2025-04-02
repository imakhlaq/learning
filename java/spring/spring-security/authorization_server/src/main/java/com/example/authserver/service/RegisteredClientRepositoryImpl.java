package com.example.authserver.service;

import com.example.authserver.entity.Client;
import com.example.authserver.repo.IClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

//@AllArgsConstructor
//@Component
//public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
//
//    private final IClientRepo clientRepo;
//
//    @Override
//    public void save(RegisteredClient registeredClient) {
//
//        clientRepo.save(Client.from(registeredClient));
//    }
//
//    @Override
//    public RegisteredClient findById(String id) {
//
//        var client = clientRepo.findById(Long.valueOf(id))
//            .orElseThrow(() -> new IllegalArgumentException("Client not found"));
//
//        return Client.from(client);
//    }
//
//    @Override
//    public RegisteredClient findByClientId(String clientId) {
//
//        var client = clientRepo.findClientsByClientId(clientId)
//            .orElseThrow(() -> new IllegalArgumentException("Client not found"));
//        return Client.from(client);
//    }
//}

//don't use this in memory instead used the above to retrive details from db
//create a route to create a Client just like user
@Configuration
public class RegisteredClientRepositoryImpl {

    //this represents one client that is registered with the auth server
    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        String GATEWAYCLIENTID = "gateway-client";
        String GATEWAYCLIENT_HOSTURL = "http://localhost:8080";

        var webClient = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientSecret("secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri(GATEWAYCLIENT_HOSTURL + "/login/oauth2/code/" + GATEWAYCLIENTID)
            .postLogoutRedirectUri(GATEWAYCLIENT_HOSTURL + "/logout")
            .scope(OidcScopes.OPENID)  // openid scope is mandatory for authentication
            .scope(OidcScopes.PROFILE)
            .scope(OidcScopes.EMAIL)
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat.REFERENCE)//means the non opack token
                    .accessTokenTimeToLive(Duration.ofSeconds(9000))
                    .reuseRefreshTokens(false)
                    .build()
            )
            .build();

        String PUBLICCLIENTID = "public-client";
        String PUBLICCLIENT_HOSTURL = "http://localhost:8080";

        RegisteredClient publicWebClient = RegisteredClient
            .withId(UUID.randomUUID().toString())
            .clientId(PUBLICCLIENTID)
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE) // authentication method set to 'NONE'
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri(PUBLICCLIENT_HOSTURL + "/callback")
            .postLogoutRedirectUri(PUBLICCLIENT_HOSTURL + "/logout")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope(OidcScopes.EMAIL)
            .clientSettings(ClientSettings.builder().requireProofKey(true).build()) // enable PKCE
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofSeconds(7000))
                    .build()
            )
            .build();

        return new InMemoryRegisteredClientRepository(publicWebClient, webClient);
    }
}