package com.kolya.gym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class BearerTokenFilter extends GenericFilterBean {

    @Value("${auth.bearer.token}")
    private String bearerToken;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            sendError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Bearer token is missing");
            return;
        }

        String token = authorizationHeader.substring(7);

        if (!token.equals(bearerToken)) {
           sendError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Invalid Bearer token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        String json = new ObjectMapper().writeValueAsString(Collections.singletonMap("error", message));

        response.getWriter().write(json);
    }
}
