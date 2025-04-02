package com.example.securitywithbasicauth.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ConditionalEvaluator {

    public boolean condition(String sp) {

        var security = SecurityContextHolder.getContext().getAuthentication();

        //code

        //and true if case you want to allow
        return true;
    }
}