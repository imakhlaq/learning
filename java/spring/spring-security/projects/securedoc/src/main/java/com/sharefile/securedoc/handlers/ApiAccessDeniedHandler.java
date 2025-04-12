package com.sharefile.securedoc.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sharefile.securedoc.utils.RequestUtils.handleErrorResponse;

/*
SimpleUrlAuthenticationFailureHandler redirects users to a specified URL when authentication fails (e.g., incorrect username/password),
while AccessDeniedHandler handles situations where a user is authenticated but lacks the necessary permissions to access a protected resource,
typically resulting in a 403 Forbidden response.
 */
@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        handleErrorResponse(request, response, exception);
    }
}