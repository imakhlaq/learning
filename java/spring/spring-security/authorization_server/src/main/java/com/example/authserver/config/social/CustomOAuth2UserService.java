package com.example.authserver.config.social;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * CustomOAuth2UserService is used to customize the user details fetched from the Oauth provider.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        // Fetch user info from the provider
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //create and store user in DB
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        //you can customize user Oauth2 use to have roles and other fields
        return new DefaultOAuth2User(
            Collections.singleton(() -> "ROLE_USER"), // Roles
            oAuth2User.getAttributes(), // User attributes from provider
            name // Identifier attribute key
        );
    }
}