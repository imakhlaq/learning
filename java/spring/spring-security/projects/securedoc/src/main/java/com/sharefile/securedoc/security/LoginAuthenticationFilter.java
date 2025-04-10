package com.sharefile.securedoc.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharefile.securedoc.domain.Response;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.dtorequest.LoginRequest;
import com.sharefile.securedoc.enumeration.LoginType;
import com.sharefile.securedoc.enumeration.TokenType;
import com.sharefile.securedoc.service.JwtService;
import com.sharefile.securedoc.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.sharefile.securedoc.utils.RequestUtils.getResponse;
import static com.sharefile.securedoc.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Component
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String LOGIN_PATH = "/user/login";
    /*
     Spring in build providers uses the UserDetailsService implementation to load the user and check credentials.
     But here we are using our own UserService to do this
     */
    private final UserService userService;
    private final JwtService jwtService;

    protected LoginAuthenticationFilter(AuthenticationManager authenticationManager,
                                        UserService userService,
                                        JwtService jwtService) {

        //route on which the login request will arrive
        super(new AntPathRequestMatcher(LOGIN_PATH, POST.name()), authenticationManager);

        this.userService = userService;
        this.jwtService = jwtService;
    }
    //note you cant use try catch here else unsuccessfulAuthentication () method will not execute
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            //Grab the user information to create the authentication after getting the login types
            var userIdPass = new ObjectMapper()
                .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
                .readValue(request.getInputStream(), LoginRequest.class);

            userService.updateLoginAttempt(userIdPass.getEmail(), LoginType.LOGIN_ATTEMPT);
            log.debug("Attempting to authenticate user {}", userIdPass.getEmail());

            var authenticationObject = ApiAuthenticationObject.unAuthenticated(userIdPass.getEmail(),
                userIdPass.getPassword());

            // getAuthenticationManager is parent class method used to get AuthenticationManager,
            //AuthenticationManager is passed to parent using the super(......)
            return getAuthenticationManager()
                .authenticate(authenticationObject); //Pass the credentials to the authentication manager

        } catch (IOException e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    /**
     * Handles successful authentication by updating user login attempt and responding based on MFA status
     *
     * @param request    - the HttpServletRequest object
     * @param response   - the HttpServletResponse object
     * @param chain      - the FilterChain object
     * @param authResult - the Authentication object
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {

        // Get the authenticated user
        var user = (User) authResult.getPrincipal();
        log.debug("Successfully authenticated user {}", user.getEmail());
        // Update the user's login attempt
        userService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);

        //determine the isMfa is active or not and base on that send response to user
        var httpResponse = user.isMfa() ? sendQrCode(request, user) : sendResponse(request, response, user);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        // Write response to output stream
        var out = response.getWriter();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();

    }
    //when Mfa is not active create Access and Refresh token and send it to user
    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        // Add access token cookie
        jwtService.addCookie(response, user, TokenType.ACCESS);
        // Add refresh token cookie
        jwtService.addCookie(response, user, TokenType.REFRESH);
        // Return a response with user information and success message
        return getResponse(request, Map.of("user", user), "Login Success", HttpStatus.OK);
    }

    //when Mfa is active send a QR code
    private Response sendQrCode(HttpServletRequest request, User user) {
        return getResponse(request, Map.of("user", user), "Please enter QR code", HttpStatus.OK);
    }
}