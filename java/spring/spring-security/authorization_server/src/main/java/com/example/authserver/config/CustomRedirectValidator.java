package com.example.authserver.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;

import java.util.function.Consumer;



/*
By default, callback url to local host is not supported.
So using this you can specify which type of call back are supported which are not
 */

public class CustomRedirectValidator implements Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> {
    @Override
    public void accept(OAuth2AuthorizationCodeRequestAuthenticationContext context) {

        OAuth2AuthorizationCodeAuthenticationToken a = context.getAuthentication();
        var registeredClient = context.getRegisteredClient();
        var uri = a.getRedirectUri();
        if (!registeredClient.getRedirectUris().contains(uri)) {
            var err = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);

            throw new OAuth2AuthorizationCodeRequestAuthenticationException(err, null);
        }

    }
}