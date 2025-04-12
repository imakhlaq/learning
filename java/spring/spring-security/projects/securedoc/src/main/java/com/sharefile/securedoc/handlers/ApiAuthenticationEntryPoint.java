package com.sharefile.securedoc.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sharefile.securedoc.utils.RequestUtils.handleErrorResponse;

/*
AuthenticationEntryPoint:
It's invoked when a user tries to access a protected resource but hasn't provided any authentication credentials or the credentials are invalid.
It's responsible for initiating the authentication flow, such as redirecting to a login page or prompting for credentials via an HTTP 401 response.

SimpleUrlAuthenticationFailureHandler:
This handler is used to handle the case when a user's authentication attempt fails.
It typically redirects the user to a specified URL (usually a login page) after a failed login attempt.

AccessDeniedHandler:
This handler is triggered when a user is authenticated but does not have the necessary permissions to access a protected resource.
It often results in a 403 Forbidden response, indicating that the user is not authorized to access the requested resource.
 */

/*
When a user tries to access a resource that requires authentication and doesn't have a valid authentication token or credentials,
the AuthenticationEntryPoint is invoked.
 */
@Component //Because they have to be a bean
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        handleErrorResponse(request, response, exception);
    }
}