package com.example.authserver.config.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Map;

/**
 * CustomAccessDeniedHandler is used to handle the denied exception by Oauth client.
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    final private String logInURl = "/login";
    final private ObjectMapper objectMapper = new ObjectMapper();

//    public CustomAccessDeniedHandler(String logInURl) {
//        this.logInURl = logInURl;
//    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("OAuth client denied access");

        // Set response headers
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        var res = Map.of("redirectUrl", logInURl);
        objectMapper.writeValue(response.getWriter(), res);
    }
}