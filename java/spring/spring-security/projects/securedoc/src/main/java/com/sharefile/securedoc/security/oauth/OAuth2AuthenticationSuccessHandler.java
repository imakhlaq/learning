package com.sharefile.securedoc.security.oauth;

import com.sharefile.securedoc.domain.UserPrincipal;
import com.sharefile.securedoc.enumeration.TokenType;
import com.sharefile.securedoc.service.JwtService;
import com.sharefile.securedoc.service.UserService;
import com.sharefile.securedoc.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.sharefile.securedoc.security.oauth.CustomStatelessAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/*
On successful authentication, Spring security invokes the onAuthenticationSuccess() method of the OAuth2AuthenticationSuccessHandler configured in SecurityConfig.

In this method, we perform some validations, create a JWT authentication token,
 and redirect the user to the redirect_uri specified by the client with the JWT token added in the query string -
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CustomStatelessAuthorizationRequestRepository customStatelessAuthorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        var principal = (UserPrincipal) authentication.getPrincipal();

        //   var principal = (UserPrincipal) authentication.getPrincipal();

        //attach the access and refresh cookies
        jwtService.addCookie(response, principal.getUser(), TokenType.ACCESS);
        jwtService.addCookie(response, principal.getUser(), TokenType.REFRESH);

//        String targetUrl = determineTargetUrl(request, response, authentication);
//
//        if (response.isCommitted()) {
//            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
//            return;
//        }

        clearAuthenticationAttributes(request, response);
        //  getRedirectStrategy().sendRedirect(request, response, targetUrl);
        response.sendRedirect("http://localhost:5173/dashboard");
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        customStatelessAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    //if u have multiple clients like android/ios/browser u need to create a different uri
    @SneakyThrows
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        Optional<String> redirectUri =
            CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        return UriComponentsBuilder.fromUriString(targetUrl)
            .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        var allowedRedirectUris = List.of(
            "http://localhost:5173/dashboard", //request will be redirected here after authentication
            //  "http://localhost:3000/oauth2/redirect", //request will be redirected here after authentication
            "myandroidapp://oauth2/redirect",
            "myiosapp://oauth2/redirect");

        return allowedRedirectUris
            .stream()
            .anyMatch(authorizedRedirectUri -> {
                // Only validate host and port. Let the clients use different paths if they want to
                URI authorizedURI = URI.create(authorizedRedirectUri);
                //comparing the redirect uri send by frontend to the allowed uris on the backend
                if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                    && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                    return true;
                }
                return false;
            });
    }
}