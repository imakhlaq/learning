package com.example.authserver.config;

import com.example.authserver.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

//to customize the JWT
@RequiredArgsConstructor
@Slf4j
public class Oauth2AccessTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    // Here we are using the in memory user details service, but this could be any user service/repository
    private final UserDetailsService userService;

    @Override
    public void customize(JwtEncodingContext context) {

        log.info("Customizing Oauth2 access token");

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

            log.info("Customizing access token");
            context.getClaims().claims(claims -> {
                Object principal = context.getPrincipal().getPrincipal();

                // STARTS HERE
                User user = null; // TODO when add the db fix this user to be security user

                if (principal instanceof UserDetails) { // form login
                    user = (User) principal;
                } else if (principal instanceof DefaultOidcUser oidcUser) { // oauth2 login
                    // fetch user by email to obtain User object when principal is not already a User object
                    String email = oidcUser.getEmail();
                    user = (User) userService.loadUserByUsername(email);
                }

                if (user == null) return;
                // ENDS HERE

                Set<String> roles = AuthorityUtils.authorityListToSet(user.getAuthorities())
                    .stream().map(c -> c.replaceFirst("^ROLE_", ""))
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