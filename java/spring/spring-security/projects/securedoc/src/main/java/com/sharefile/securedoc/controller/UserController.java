package com.sharefile.securedoc.controller;

import com.sharefile.securedoc.domain.Response;
import com.sharefile.securedoc.dtorequest.UserRequest;
import com.sharefile.securedoc.handlers.ApiLogoutHandler;
import com.sharefile.securedoc.service.JwtService;
import com.sharefile.securedoc.service.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.sharefile.securedoc.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final JwtService jwtservice;
    private final ApiLogoutHandler logoutHandler;

    @PostMapping("/register")//because of @Valid spring will validate the req body
    public ResponseEntity<Response> register(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(
            user.getFirstName(),
            user.getLastName(),
            user.getPassword(),
            user.getEmail());

        return ResponseEntity.created(getUri())
            .body(getResponse(request, Map.of(), "Account is created check email to activate your account", HttpStatus.CREATED));
    }

    @PostMapping("/verify/account")//because of @Valid spring will validate the req body
    public ResponseEntity<Response> verifyToken(@RequestParam("toke") String token, HttpServletRequest request) {

        userService.verifyToken(token);

        return ResponseEntity.created(getUri())
            .body(getResponse(request, Map.of(), "Account is verified", HttpStatus.CREATED));
    }

    // Cant test this is postman, we test when we create the frontend
    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //User user = (User) authentication.getPrincipal(); //This is what an @AuthenticationPrincipal is doing
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "You've logged out successfully", OK));
    }
    private URI getUri() {
        return URI.create("");
    }

}