package com.example.real.estate.admin.RealEstateAdmin.config.security;

import com.example.real.estate.admin.RealEstateAdmin.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static java.util.Objects.nonNull;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        if (nonNull(authentication.getPrincipal())) {
            User user = (User) authentication.getPrincipal();
        }
        response.sendRedirect("/users");
    }

}
