package com.example.securitywithbasicauth.controller;

import com.example.securitywithbasicauth.config.security.authentication.ApiAuthenticationObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        //currently ApiAuthenticationObject only have key(using as username)
        //do same in the filter and set the security context (extreact the user pass from JWT or cookie)
        var auth = authenticationManager.authenticate(new ApiAuthenticationObject(username));

        //if code comes here means authentication is success

        return ResponseEntity.ok(auth.isAuthenticated());
    }
}