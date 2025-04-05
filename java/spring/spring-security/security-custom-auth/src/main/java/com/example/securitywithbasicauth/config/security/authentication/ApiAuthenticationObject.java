package com.example.securitywithbasicauth.config.security.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

// it represents the Authentication object
@Setter
@Getter
public class ApiAuthenticationObject implements Authentication {

    //when auth is successful update the isAuthenticated true
    private boolean isAuthenticated;
    private Object credentials;
    private final String apiKey;

    public ApiAuthenticationObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}

/*

Check UsernamePasswordAuthenticationToken implementation it has two static methods
        - one to get unauthenticated object
            when you get credentials from user( username and pass) using this you create unauthenticated object
            and pass it to provider and provider gives you an authenticated object

        - one to get authenticated object ( used by the AuthenticationProvider to convert unauthenticated object to authenticated,
          and it contains principle, authorities  check below)


    public ApiAuthenticationObject(Object principal, Object credentials) {

        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public ApiAuthenticationObject(Object principal, Object credentials,
                                   Collection<? extends GrantedAuthority> authorities) {

        this.credentials = credentials;
        setAuthenticated(true); // must use super, as we override
    }

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }


    public static UsernamePasswordAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials);
    }

 */