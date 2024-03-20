package com.kolya.gym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.exception.InvalidTokenException;
import com.kolya.gym.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        super();
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = null;
        String jwt = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null){
            jwt = getJwtFromCookie(cookies);
        }
        if (jwt!=null){
            username = jwtService.extractUsernameOrReturnNull(jwt);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && jwtService.isTokenValid(jwt)){
            setAuthenticationForContext(username, request);
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        String json = new ObjectMapper().writeValueAsString(Collections.singletonMap("error", message));
        response.getWriter().write(json);
    }

    private String getJwtFromCookie(Cookie[] cookies){
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void setAuthenticationForContext(String username, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}
