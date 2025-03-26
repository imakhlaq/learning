package com.example.securitywithbasicauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    //this bean is used by spring security to get the user details from the database
    //when u UserDetailsService then spring will not provide default id and password to login
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        //encoder that doesn't encode the password
        return NoOpPasswordEncoder.getInstance();
    }
}