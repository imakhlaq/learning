package com.example.authserver.service;

import com.example.authserver.repo.IUserRepo;
import com.example.authserver.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private IUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return new SecurityUser(user);
    }
}

//@Configuration
//@AllArgsConstructor
//public class UserDetailsServiceImpl {
//
//    public final PasswordEncoder passwordEncoder;
//
//    //REMOVED because we added the UserDetailsService as bean
//    //this represents one user that is registered with the auth server
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        var user = User
//            .withUsername("bill")
//            .password(passwordEncoder.encode("123"))
//            .roles("USER")
//            .build();
//
//        UserDetails me = User.builder()
//            .username("afeefrazickamir@gmail.com")
//            .password(passwordEncoder.encode("pass"))
//            .roles("USER", "ADMIN")
//            .build();
//        // @formatter:on
//
//        return new InMemoryUserDetailsManager(user, me);
//
//    }
//}