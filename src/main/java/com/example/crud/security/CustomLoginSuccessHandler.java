package com.example.crud.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/dashboardadmin");
        } else if (role.equals("ROLE_USER")) {
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/access-denied");
        }
    }
}