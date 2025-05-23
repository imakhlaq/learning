package com.sharefile.securedoc.domain;

import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.entity.UserCredentialEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/*
Spring boot need UserDetails
 */
@AllArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {
    @Getter
    private final User user;
    private final UserCredentialEntity userCredential;

    @Override
    public Map<String, Object> getAttributes() {
        return user.getAttributes();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities());
    }
    @Override
    public String getPassword() {
        return userCredential.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNonExpired();
    }
    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNonLocked();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNonExpired();
    }
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
    @Override
    public String getName() {
        return user.getUserId();
    }
}