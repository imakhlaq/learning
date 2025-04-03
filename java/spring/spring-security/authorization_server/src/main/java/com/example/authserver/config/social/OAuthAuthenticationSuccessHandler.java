package com.example.authserver.config.social;

import com.example.authserver.repo.IUserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * OAuthLoginSuccessHandler class is used to handle the successful login of the user.
 */
@AllArgsConstructor
public final class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final IUserRepo userRepo;

    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            var user = authentication.getPrincipal();

            if (user instanceof OidcUser u) {

            } else if (user instanceof OAuth2User u) {

                // Capture user in a local data store on first authentication
                if (this.userRepo.findByUsername(u.getAttribute("email")).isEmpty()) {
                    //save the user in db
                    System.out.println("Saving first-time user: name=" + u.getName()
                        + ", claims=" + u.getAttributes()
                        + ", authorities=" + u.getAuthorities());

                    // this.userRepo.save(user);
                }
            }
        }
        this.delegate.onAuthenticationSuccess(request, response, authentication);
    }
}