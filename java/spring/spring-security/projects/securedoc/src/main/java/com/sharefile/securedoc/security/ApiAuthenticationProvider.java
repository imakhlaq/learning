package com.sharefile.securedoc.security;

import com.sharefile.securedoc.domain.UserPrincipal;
import com.sharefile.securedoc.exception.ApiException;
import com.sharefile.securedoc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
/*
 * Custom authentication provider for handling user authentication.
 */
public class ApiAuthenticationProvider implements AuthenticationProvider {

    public static final int DAYS = 90;
    /**
     * Service for managing user-related operations.
     */
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Consumer to validate user account status.
     */
    private final Consumer<UserPrincipal> validAccount = (userPrincipal) -> {
        if (!userPrincipal.isAccountNonLocked()) throw new LockedException("Account is locked");
        if (!userPrincipal.isAccountNonExpired()) throw new DisabledException("Account is expired");
        if (!userPrincipal.isCredentialsNonExpired()) throw new CredentialsExpiredException("Credentials are expired");
        if (!userPrincipal.isEnabled()) throw new DisabledException("User account is enabled");
    };

    /**
     * Authenticates the user based on the provided authentication information.
     *
     * @param authentication The authentication object containing user credentials.
     * @return An Authentication object if the user is successfully authenticated.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //type casting UnAuthentication Object
        var apiAuthenticationObject = (ApiAuthenticationObject) authentication;
        //using the email provided by the user(apiUnAuthenticationObject) to get the user from DB
        var user = userService.getUserByEmail(apiAuthenticationObject.getEmail());

        //check if user exits
        if (user != null) {
            //if user exits get its credentials
            var userCredential = userService.getUserCredentialById(user.getId());
            //After getting the credentials we have to check if the credentials is expired.
            if (userCredential.getUpdatedAt().minusDays(DAYS).isAfter(LocalDateTime.now())) {
                throw new ApiException("Your credentials have expired");
            }
            //creating custom UserPrinciple
            var userPrinciple = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrinciple);//validating account isLocked,Expired etc..

            if (passwordEncoder.matches(apiAuthenticationObject.getPassword(), userCredential.getPassword())) {
                return ApiAuthenticationObject.authenticated(user, userPrinciple.getAuthorities());
            } else throw new BadCredentialsException("Invalid email or password");
        } else throw new ApiException("Invalid email or password");

    }
    /**
     * Determines if this authentication provider supports the authentication class provided.
     *
     * @param authentication The authentication class to check.
     * @return True if the authentication class is supported, false otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthenticationObject.class.isAssignableFrom(authentication);
    }
}