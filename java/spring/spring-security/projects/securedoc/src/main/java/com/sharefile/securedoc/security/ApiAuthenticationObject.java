package com.sharefile.securedoc.security;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.exception.ApiException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

@Setter
@Getter
public class ApiAuthenticationObject extends AbstractAuthenticationToken {
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

    //creates Authenticated authentication object
    private ApiAuthenticationObject(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.isAuthenticated = true;
        this.password = "[PROTECTED]";//when authentication is successful hiding the password
    }

    public static ApiAuthenticationObject unAuthenticated(String email, String password) {
        return new ApiAuthenticationObject(email, password);
    }

    public static ApiAuthenticationObject authenticated(User user, Collection<? extends GrantedAuthority> authorities) {
        return new ApiAuthenticationObject(user, authorities);
    }

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