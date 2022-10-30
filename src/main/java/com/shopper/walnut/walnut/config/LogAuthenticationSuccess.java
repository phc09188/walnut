package com.shopper.walnut.walnut.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class LogAuthenticationSuccess implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String url = "/";
        String why = authentication.getAuthorities().toString();
        switch (authentication.getAuthorities().toString()){
            case "[ROLE_ADMIN]":
                url = "/admin/main.do";
                break;
            case "[ROLE_BRAND]":
                url = "/brand/main/detail.do";
                break;
        }
        response.sendRedirect(url);
    }
}
