package com.example.securitywithbasicauth.config.security.providers;

import com.example.securitywithbasicauth.config.security.authentication.ApiAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/*
      Filter (creates unAuthenticated Authentication object)
        |
Authentication Manager (receives the unauthenticated Authentication object and passes the authentication provider)
        |
Authentication Provider (receives the unauthenticated Authentication object and authenticate and return an
                         authenticated Authentication object)


 */
@AllArgsConstructor
public class ApiAuthProvider implements AuthenticationProvider {

    private final String secretKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var apiAuthentication = (ApiAuthentication) authentication;

        String headerKey = apiAuthentication.getApiKey();

        if (secretKey.equals(headerKey)) {
            //after successful auth creating a new authentication object
            var auth = new ApiAuthentication(null);
            auth.setAuthenticated(true);
            return auth;
        }

        throw new BadCredentialsException("oh Nooo");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //this is used to register what type of authentication object will be authentication by this provider
        return ApiAuthentication.class.equals(authentication);
    }
}