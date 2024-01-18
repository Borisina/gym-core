package com.kolya.gym.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${secret-key}")
    private String SECRET_KEY;

    public String extractUsername(String token) throws SignatureException {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws SignatureException {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws SignatureException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) throws SignatureException {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UUID transactionId, UserDetails userDetails) {
        logger.info("Transaction ID: {}, Generating jwt for {}", transactionId, userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, userDetails.getUsername());
        logger.info("Transaction ID: {}, Jwt token generated for {}: {}", transactionId, userDetails.getUsername(), token);
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws SignatureException {
        final String username = extractUsername(token);
        return (username.equals(userDetailsService.loadUserByUsername(username).getUsername()) && !isTokenExpired(token));
    }
}