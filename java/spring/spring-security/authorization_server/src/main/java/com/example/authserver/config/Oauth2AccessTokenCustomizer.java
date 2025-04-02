package com.example.authserver.config;

import com.example.authserver.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

//to customize the JWT
@RequiredArgsConstructor
@Component
public class Oauth2AccessTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

            var authorities = context
                .getPrincipal()
                .getAuthorities();

            //adding user authorities to the JWT
            context
                .getClaims()
                .claim("authorities", authorities  //changing the authorities object to List
                    .stream()
                    .map(GrantedAuthority::getAuthority).toList());

            context.getClaims().claims(claims -> {
                Object principal = context.getPrincipal().getPrincipal();
                SecurityUser user = (SecurityUser) principal;

                Set<String> roles = AuthorityUtils.authorityListToSet(user
                        .getAuthorities())
                    .stream()
                    .map(c -> c.replaceFirst("^ROLE_", ""))
                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                claims.put("roles", roles);

                // I have only added the roles to the JWT here as I am using the limited fields
                // on the UserDetails object, but you can add many other important fields by
                // using your applications User class (as shown below)

                // claims.put("email", user.getEmail());
                // claims.put("sub", user.getId());
            });
        }
    }
}