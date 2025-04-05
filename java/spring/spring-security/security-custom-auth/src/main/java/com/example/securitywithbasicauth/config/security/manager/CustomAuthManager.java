package com.example.securitywithbasicauth.config.security.manager;

import com.example.securitywithbasicauth.config.security.providers.ApiAuthProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/*
You don't need to create an authentication manager because spring internally creates it.
And there should be only one authentication manager
 */
@AllArgsConstructor
public class CustomAuthManager implements AuthenticationManager {

    private final String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var apiAuthProvider = new ApiAuthProvider();
        //check here which authentication manager it is
        if (apiAuthProvider.supports(authentication.getClass())) {
            return apiAuthProvider.authenticate(authentication);
        }
        /*
        other authentication providers and authentication objects
         elseif (apiAuthProvider.supports(authentication.getClass())) {
            return apiAuthProvider.authenticate(authentication);
        }
         */

        throw new BadCredentialsException("oh Nooo");
    }
}