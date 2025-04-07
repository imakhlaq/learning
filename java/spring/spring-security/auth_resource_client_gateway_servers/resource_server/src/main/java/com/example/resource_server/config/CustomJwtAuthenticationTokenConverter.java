package com.example.resource_server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

/*
 From the token(jwt) taking out the authorities and adding them to context
 */
public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, JwtAuthenticationToken> {
    @Override
    public JwtAuthenticationToken convert(Jwt source) {

        List<String> authorities = (List<String>) source
            .getClaims()
            .get("authorities");

        //adding granted authorities to the authentication object
        var authenticationObject = new JwtAuthenticationToken(source,
            authorities
                .stream()
                .map(SimpleGrantedAuthority::new).toList());

        return authenticationObject;
    }
}


/*
public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, CustomJwtAuthenticationToken> {
    @Override
    public CustomJwtAuthenticationToken convert(Jwt source) {

        List<String> authorities = (List<String>) source
            .getClaims()
            .get("authorities");

        //adding granted authorities to the authentication object
        var authenticationObject = new CustomJwtAuthenticationToken(source,
            authorities
                .stream()
                .map(SimpleGrantedAuthority::new).toList());

        return authenticationObject;
    }
}
 */