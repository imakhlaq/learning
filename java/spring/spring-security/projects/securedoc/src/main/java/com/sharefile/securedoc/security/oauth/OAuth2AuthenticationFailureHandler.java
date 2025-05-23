package com.sharefile.securedoc.security.oauth;

import com.sharefile.securedoc.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.sharefile.securedoc.security.oauth.CustomStatelessAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/*
SimpleUrlAuthenticationFailureHandler and AccessDeniedHandler handle different types of security failures: authentication failures versus authorization failures.
SimpleUrlAuthenticationFailureHandler redirects users to a specified URL when authentication fails (e.g., incorrect username/password),
while AccessDeniedHandler handles situations where a user is authenticated but lacks the necessary permissions to access a protected resource,
typically resulting in a 403 Forbidden response.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CustomStatelessAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", exception.getLocalizedMessage())
            .build().toUriString();

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}