package com.example.crud.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {
            response.sendRedirect("/dashboardadmin");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
<<<<<<< HEAD
<<<<<<< HEAD
}
=======

}
>>>>>>> 2b7fae4af8aeefafe3d05b9b875fbf4fce153a0c
=======

}
>>>>>>> 5ea7ae70922e06c7c6e3045089bef903756a1be7
