package com.example.authserver.config.social;

import com.example.authserver.security.SecurityUser;
import com.example.authserver.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * UserDetails service is used to load user from db and DefaultOAuth2UserService is used to load
 * user from auth provider
 * CustomOAuth2UserService is used to customize the user details fetched from the Oauth provider.
 */
@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        // loading/fetching user details from auth provider
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //create and store user in DB
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        //you can customize user Oauth2 use to have roles and other fields
        var customizedOauth2 = new DefaultOAuth2User(
            Collections.singleton(() -> "ROLE_USER"), // Roles
            oAuth2User.getAttributes(), // User attributes from provider
            name // Identifier attribute key
        );

        return buildPrincipal(customizedOauth2);
    }

    /**
     * Builds the security principal from the given userRequest.
     * Registers the user if not already registered
     */
    public SecurityUser buildPrincipal(OAuth2User oath2User) {

        Map<String, Object> attributes = oath2User.getAttributes();
        //taking out email from the oath2User
        String email = (String) attributes.get(StandardClaimNames.EMAIL);

        var user = (SecurityUser) userDetailsService.loadUserByUsername(email);

        return user;
    }
}