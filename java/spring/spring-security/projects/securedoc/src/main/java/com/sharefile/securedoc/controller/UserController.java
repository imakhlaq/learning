package com.sharefile.securedoc.controller;

import com.sharefile.securedoc.domain.Response;
import com.sharefile.securedoc.dtorequest.UserRequest;
import com.sharefile.securedoc.service.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.sharefile.securedoc.utils.RequestUtils.getResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;

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
    private URI getUri() {
        return URI.create("");
    }

}