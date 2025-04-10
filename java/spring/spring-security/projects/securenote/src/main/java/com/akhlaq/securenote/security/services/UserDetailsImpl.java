package com.akhlaq.securenote.security.services;

import com.akhlaq.securenote.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private boolean is2faEnabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, boolean is2faEnabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.is2faEnabled = is2faEnabled;
        this.authorities = authorities;
    }

    public static UserDetailsImpl builder(User user) {
        var authorities = new SimpleGrantedAuthority(user.getRole().toString());

        return new UserDetailsImpl(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.isTwoFactorEnabled(),
            List.of(authorities)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
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

    public boolean is2faEnabled() {
        return is2faEnabled;
    }
}