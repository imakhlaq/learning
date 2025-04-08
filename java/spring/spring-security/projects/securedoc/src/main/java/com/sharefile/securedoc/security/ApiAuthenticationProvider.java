package com.sharefile.securedoc.security;

import com.sharefile.securedoc.domain.UserPrincipal;
import com.sharefile.securedoc.exception.ApiException;
import com.sharefile.securedoc.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.CredentialException;
import javax.security.auth.login.CredentialExpiredException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {

    public static final int DAYS = 90;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    private final Consumer<UserPrincipal> validAccount = (userPrincipal) -> {
        if (userPrincipal.isAccountNonLocked()) throw new LockedException("Account is locked");
        if (userPrincipal.isAccountNonExpired()) throw new DisabledException("Account is expired");
        if (userPrincipal.isCredentialsNonExpired()) throw new CredentialsExpiredException("Credentials are expired");
        if (userPrincipal.isEnabled()) throw new DisabledException("User account is enabled");
    };

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthenticationObject = (ApiAuthenticationObject) authentication;
        var user = userService.getUserByEmail(apiAuthenticationObject.getEmail());

        if (user != null) {
            var userCredential = userService.getUserCredentialById(user.getId());

            if (userCredential.getUpdatedAt().minusDays(DAYS).isAfter(LocalDateTime.now())) {
                throw new ApiException("Your credentials have expired");
            }
            var userPrinciple = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrinciple);//validating account isLocked,Expired etc..

            if (passwordEncoder.matches(apiAuthenticationObject.getPassword(), userCredential.getPassword())) {
                return ApiAuthenticationObject.authenticated(user, userPrinciple.getAuthorities());
            } else throw new BadCredentialsException("Invalid email or password");

        } else throw new ApiException("Invalid email or password");

    }
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}