package com.sharefile.securedoc.handlers;

import com.sharefile.securedoc.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import static com.sharefile.securedoc.enumeration.TokenType.ACCESS;
import static com.sharefile.securedoc.enumeration.TokenType.REFRESH;

@RequiredArgsConstructor
@Service
public class ApiLogoutHandler implements LogoutHandler {
    // We need this to remove the cookies when the user logout.
    private final JwtService jwtService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var logoutHandler = new SecurityContextLogoutHandler();
        // Logout Handler handle the logout for us and clean things up
        logoutHandler.logout(request, response, authentication);
        // We need to remove the cookie
        jwtService.removeCookie(request, response, ACCESS.getValue());
        jwtService.removeCookie(request, response, REFRESH.getValue());
    }
}