package com.example.securitywithbasicauth.security;

import com.example.securitywithbasicauth.entity.User;
import com.example.securitywithbasicauth.service.SecurityAuthority;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SecurityUser implements UserDetails {

    final private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user
            .getAuthorities()
            .stream()
            .map(SecurityAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
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
}