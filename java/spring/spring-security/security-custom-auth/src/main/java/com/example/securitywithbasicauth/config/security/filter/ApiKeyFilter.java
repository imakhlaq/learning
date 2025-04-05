package com.example.securitywithbasicauth.config.security.filter;

import com.example.securitywithbasicauth.config.security.authentication.ApiAuthenticationObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

/*
Instead of extending Filter class use OncePerRequestFilter, and it will be called only once
 */

public class ApiKeyFilter extends OncePerRequestFilter {

    private final String apiKey;
    //injecting authentication manager to perform user authentication by taking out the user data from JWT or session
    private final AuthenticationManager authenticationManager;

    public ApiKeyFilter(String apiKey, AuthenticationManager authenticationManager) {
        this.apiKey = apiKey;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1 crate an authentication object which is not yet authenticated
        String requestHeaderKey = request.getHeader("x-api-key");

        //this will call the next filter for another authentication
        if ("null".equals(requestHeaderKey) || requestHeaderKey == null) {
            filterChain.doFilter(request, response);
        }

        var unAuthenticatedObject = new ApiAuthenticationObject(requestHeaderKey);

        try {

            // 2 delegate the authentication object to the manager

            var auth = authenticationManager.authenticate(unAuthenticatedObject);

            // 3 get back the authentication from manager

            // 4 if the object is authenticated then send request to the next filter in the chain
            if (auth.isAuthenticated()) {
                SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);
                filterChain.doFilter(request, response); //calling the next filter in filter chain
            }

        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(e.getMessage());
        }
    }
}