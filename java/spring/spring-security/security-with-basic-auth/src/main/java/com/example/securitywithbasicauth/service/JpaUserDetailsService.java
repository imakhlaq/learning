package com.example.securitywithbasicauth.service;

import com.example.securitywithbasicauth.repo.UserRepo;
import com.example.securitywithbasicauth.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
//this bean is used by spring security to get the user details from the database
//when use UserDetailsService then spring will not provide default id and password to login
//storing and retrieving UserDetails from DB
public class JpaUserDetailsService implements UserDetailsService {
    final private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepo.findByUsername(username);

        return u.map(SecurityUser::new)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

/*
//storing and retrieving UserDetails from in memory
    @Bean
    public UserDetailsService userDetailsService() {

        // to store the user details in memory
        var uds = new InMemoryUserDetailsManager();

        var u1 = User.withUsername("bill")
            .password("123")
            .authorities("read")
            .build();

        uds.createUser(u1);
        return uds;
    }
 */