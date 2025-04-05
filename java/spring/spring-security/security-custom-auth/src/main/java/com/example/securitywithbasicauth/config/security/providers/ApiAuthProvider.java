package com.example.securitywithbasicauth.config.security.providers;

import com.example.securitywithbasicauth.config.security.authentication.ApiAuthenticationObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/*
      Filter (creates unAuthenticated Authentication object)
        |
Authentication Manager (receives the unauthenticated Authentication object and passes the authentication provider)
        |
Authentication Provider (receives the unauthenticated Authentication object and authenticate and return an
                         authenticated Authentication object)


 */
@Component
@RequiredArgsConstructor
public class ApiAuthProvider implements AuthenticationProvider {

    @Value("${secret.key}")
    private String secretKey;
    
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //u can cast it safely because this provider will only be executed for ApiAuthenticationObject
        var apiAuthentication = (ApiAuthenticationObject) authentication;

        /*
        Here we are simply checking the key provide in the headers. but if you change the authentication object
        to store username and password provided by user. then using userDetailsService you can get the user from db and can check if user pass's
        is same or not.
        Here you can write any custom logic for authentication. (e.g. only if user is email verified they only he can login)
        and where you wat to perform the authentication
        1- /login
               simply inject the Authentication manager

               @Autowire
                  private AuthenticationManager authenticationManager;

                  authenticationManager.authenticate(new ApiAuthenticationObject(username,password);
                  and ApiAuthProvider will perform the authentication and return the authenticated ApiAuthenticationObject


         */

        String headerKey = apiAuthentication.getApiKey();

        if (secretKey.equals(headerKey)) {
            //after successful auth creating a new authentication object
            var auth = new ApiAuthenticationObject(null);
            auth.setAuthenticated(true);
            return auth;
        }

        throw new BadCredentialsException("oh Nooo");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //this is used to register what type of authentication object will be authentication by this provider
        return ApiAuthenticationObject.class.equals(authentication);
    }
}