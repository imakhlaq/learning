package com.example.authserver.security;

import com.example.authserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@Setter
public class SecurityUser implements UserDetails, OidcUser {

    final private User user;
    private Map<String, Object> claims;
    private OidcUserInfo userInfo;
    private OidcIdToken idToken;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user
            .getAuthorities()
            .stream()
            //.map(SecurityAuthority::new)
            .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Map<String, Object> getClaims() {
        return Map.of();
    }
    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }
    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
    @Override
    public String getName() {
        return "";
    }
}