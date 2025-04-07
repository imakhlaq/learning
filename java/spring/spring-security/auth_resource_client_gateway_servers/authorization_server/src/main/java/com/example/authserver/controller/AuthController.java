package com.example.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/sign_up")
    public String signUpPage() {
        return "signup";
    }

    @PostMapping("/sign_up")
    public String signUp() {
        return "signup";
    }
}