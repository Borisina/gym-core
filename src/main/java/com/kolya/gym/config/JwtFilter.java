package com.kolya.gym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.service.JwtService;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
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
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        String userName = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwt = cookie.getValue();
                    try{
                        userName = jwtService.extractUsername(jwt);
                    }catch (MalformedJwtException | SignatureException e){
                        sendError(response,HttpServletResponse.SC_FORBIDDEN, "Jwt is not correct");
                        return;
                    }
                    break;
                }
            }
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            try {
                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else {
                    sendError(response,HttpServletResponse.SC_FORBIDDEN, "Jwt is not valid");
                    return;
                }
            } catch (SignatureException e) {
                sendError(response,HttpServletResponse.SC_FORBIDDEN, "Jwt is not correct");
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        // Convert the error message into JSON
        String json = new ObjectMapper().writeValueAsString(Collections.singletonMap("error", message));

        response.getWriter().write(json);
    }
}
