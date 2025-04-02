package com.example.oauthclient.contoller;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DemoController {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @GetMapping("/token")
    public String token() {
        var request = OAuth2AuthorizeRequest.withClientRegistrationId("example")
            .principal("client")
            .build();

        var authorizedClient = authorizedClientManager.authorize(request);
        var accessToken = authorizedClient.getAccessToken();
        var refreshToken = authorizedClient.getRefreshToken();

        return String.format("TOKEN=%s REFRESH_TOKEN=%s", accessToken.getTokenValue(), refreshToken.getTokenValue());
    }
}