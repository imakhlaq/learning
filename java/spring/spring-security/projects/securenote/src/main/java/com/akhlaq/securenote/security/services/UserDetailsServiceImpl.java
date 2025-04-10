package com.akhlaq.securenote.security.services;

import com.akhlaq.securenote.models.User;
import com.akhlaq.securenote.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbUser = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var q1 = UserDetailsImpl.builder(dbUser);
        return q1;
    }
}