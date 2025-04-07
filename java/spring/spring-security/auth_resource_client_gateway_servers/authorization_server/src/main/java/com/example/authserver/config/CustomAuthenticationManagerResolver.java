/*
IN case you resource server is authorized by other authorization server.
And sends the token to verify the token, your authorization server can verify  base on the token
 */

//package com.example.authserver.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationManagerResolver;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
//import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
//
//import java.net.http.HttpRequest;
//import java.util.Map;
//
//@Configuration
//public class CustomAuthenticationManagerResolver {
//
//    @Bean
//    public AuthenticationManagerResolver<HttpRequest> authenticationManagerResolver() {
//
//        return
//    }
//}