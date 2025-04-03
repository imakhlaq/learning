package com.example.authserver.config.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Map;

/**
 * CustomAuthenticationEntryPoint class is used to handle when the user tries to access the API without authentication.
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    final private String logInURl;
    final private ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationEntryPoint(String logInURl) {
        this.logInURl = logInURl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error("Access without authentication");

        // Set response headers
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        var res = Map.of("redirectUrl", logInURl);
        objectMapper.writeValue(response.getWriter(), res);
    }
}