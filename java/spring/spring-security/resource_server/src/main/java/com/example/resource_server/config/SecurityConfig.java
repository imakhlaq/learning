package com.example.resource_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("{authorization-server-uri}")
    private String authorizationServerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //setting up the resource server and adding the token(JWT) we are using
        http
            .oauth2ResourceServer(
                r -> r
                    .jwt(jwtConfigurer -> jwtConfigurer
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        http
            .authorizeHttpRequests(
                r ->
                    r.anyRequest().authenticated()
            );

        return http.build();
    }

    //to take out authorities from jwt and add them to authentication object
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return new CustomJwtAuthenticationTokenConverter();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        /*
        Using the public key resource server will validate the JWT token
         */
        return NimbusJwtDecoder
            .withJwkSetUri(authorizationServerUri).build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}