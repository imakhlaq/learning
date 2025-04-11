package com.example.authserver.config.social;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@AllArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        var oidcUser = super.loadUser(userRequest);
        var principal = customOAuth2UserService.buildPrincipal(oidcUser);

        principal.setClaims(oidcUser.getClaims());
        principal.setIdToken(oidcUser.getIdToken());
        principal.setUserInfo(oidcUser.getUserInfo());

        return principal;
    }
}