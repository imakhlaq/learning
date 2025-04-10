package com.sharefile.securedoc.security;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.enumeration.Authority;
import com.sharefile.securedoc.exception.ApiException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
/*
This is the equivalent of the usernamePasswordAuthenticationToken
This is the user that we are going to be working with in the application when we are
not dealing with the application or saving to the database.
We will have some kind of mapper that is going to take the user from the database and then maps
it to this class(User class) and vise versa.
 */

@Setter
@Getter
// Todo: 1. Extend the authentication
public class ApiAuthenticationObject extends AbstractAuthenticationToken {
    private static final String PASSWORD_PROTECTED = "[PASSWORD PROTECTED]";
    private static final String EMAIL_PROTECTED = "EMAIL PROTECTED]";
    private User user;
    private String email;
    private String password;
    private Boolean isAuthenticated;

    //creates unAuthenticated authentication object
    private ApiAuthenticationObject(String email, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.email = email;
        this.password = password;
        this.isAuthenticated = false;
    }

    //creates Authenticated authentication object (this will be called when the authentication is successful)
    private ApiAuthenticationObject(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.email = EMAIL_PROTECTED;
        this.password = PASSWORD_PROTECTED;//when authentication is successful hiding the password
        this.isAuthenticated = true;
    }

    //to create unauthenticated authentication object
    //Unauthenticated authentication object will be created when we first receive the username and password from the user
    public static ApiAuthenticationObject unAuthenticated(String email, String password) {
        return new ApiAuthenticationObject(email, password);
    }

    //to create authenticated authentication object
    //Authenticated authentication object will be created when user credentials are verified
    public static ApiAuthenticationObject authenticated(User user, Collection<? extends GrantedAuthority> authorities) {
        return new ApiAuthenticationObject(user, authorities);
    }

    //not allowing to set authenticated by setter (only using constructor)
    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new ApiException("Cannot set this token to trusted - use isAuthenticated instead");
    }
    @Override
    public Object getCredentials() {
        return this.password;
    }
    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    @Override
    public Object getPrincipal() {
        return this.user;
    }
}