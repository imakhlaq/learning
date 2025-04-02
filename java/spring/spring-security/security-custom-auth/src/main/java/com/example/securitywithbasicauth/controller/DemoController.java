package com.example.securitywithbasicauth.controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    //authenticated users with read and write authorities
    @GetMapping("/read")
    @PreAuthorize("hasAnyAuthority('read','write')")
    public String readAndWrite() {
        return "Hello World!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String admin() {
        return "Hello World!";
    }

    //PreAuthorize
    @GetMapping("/cond/{sp}")
    @PreAuthorize("@conditionalEvaluator.condition(#sp)") //#sp to access the variables passed to the controller
    public String condi(@PathVariable String sp) {
        return "Hello World!";
    }

    //PostAuthorize
    //it will run after the method executes
    @GetMapping("/condulp/{sp}")
    @PostAuthorize("returnObject == 'demo'") //#sp to access the variables passed to the controller
    public String condiulp(@PathVariable String sp) {
        return "Hello World!";
    }

}