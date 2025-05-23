package com.example.authserver.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class CustomOAuth2TokenGenerator {

    private final UserDetailsService userService;

    //we need to add our custom refresh token generator to the DelegatingOAuth2TokenGenerator
    @Bean
    OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        JwtGenerator jwtAccessTokenGenerator = new JwtGenerator(jwtEncoder);
        jwtAccessTokenGenerator.setJwtCustomizer(new Oauth2AccessTokenCustomizer(userService)); // jwt customizer from part 1 (optional)

        return new DelegatingOAuth2TokenGenerator(
            jwtAccessTokenGenerator,
            new OAuth2PublicClientRefreshTokenGenerator() // add customized refresh token generator
        );
    }
}