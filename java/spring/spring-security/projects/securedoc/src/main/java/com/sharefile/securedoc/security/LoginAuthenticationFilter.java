package com.sharefile.securedoc.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharefile.securedoc.domain.Response;
import com.sharefile.securedoc.dto.User;
import com.sharefile.securedoc.dtorequest.LoginUser;
import com.sharefile.securedoc.enumeration.LoginType;
import com.sharefile.securedoc.enumeration.TokenType;
import com.sharefile.securedoc.service.JwtService.JwtService;
import com.sharefile.securedoc.service.user.IUserService;
import com.sharefile.securedoc.service.user.UserServiceImpl;
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

import java.io.IOException;
import java.util.Map;

import static com.sharefile.securedoc.utils.RequestUtils.getResponse;
import static com.sharefile.securedoc.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String LOGIN_PATH = "/user/login";
    private final IUserService userService;
    private final JwtService jwtService;

    protected LoginAuthenticationFilter(AuthenticationManager authenticationManager,
                                        IUserService userService,
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
            var userIdPass = new ObjectMapper()
                .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
                .readValue(request.getInputStream(), LoginUser.class);

            userService.updateLoginAttempt(userIdPass.getEmail(), LoginType.LOGIN_ATTEMPT);

            var authenticationObject = ApiAuthenticationObject.unAuthenticated(userIdPass.getEmail(),
                userIdPass.getPassword());

            return getAuthenticationManager()
                .authenticate(authenticationObject);

        } catch (IOException e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    //when successful auth happens this callback will be called
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        var user = (User) authResult.getPrincipal();
        userService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);

        //base on user have 2-factor auth is enable make decision her
        var httpResponse = user.isMfa() ? sendQrCode(request, user) : sendResponse(request, response, user);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        var out = response.getWriter();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();

    }
    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response, user, TokenType.ACCESS);
        jwtService.addCookie(response, user, TokenType.REFRESH);
        return getResponse(request, Map.of("user", user), "Login Success", HttpStatus.OK);
    }

    private Response sendQrCode(HttpServletRequest request, User user) {
        return getResponse(request, Map.of("user", user), "Please enter QR code", HttpStatus.OK);
    }
}