package com.sharefile.securedoc.security;

import com.sharefile.securedoc.service.JwtService;
import com.sharefile.securedoc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiHttpConfigurer extends AbstractHttpConfigurer<ApiHttpConfigurer, HttpSecurity> {
    private final AuthorizationFilter authorizationFilter;
    private final ApiAuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationConfiguration authenticationConfiguration; // Need to get a hold of the authentication manager

    //OVERRIDE METHODS
    @Override
    public void init(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new LoginAuthenticationFilter(
                authenticationConfiguration.getAuthenticationManager(), userService, jwtService),
            UsernamePasswordAuthenticationFilter.class);
    }
}