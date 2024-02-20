package com.kolya.gym.service;

import com.kolya.gym.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Value("${audience-type}")
    private String AUDIENCE_TYPE;

    private String tokenForServices;

    public String extractUsername(String token) throws SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractAudience(String token) throws SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getAudience);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws SignatureException, ExpiredJwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws SignatureException, ExpiredJwtException {
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

    public boolean isTokenValid(String token) {
        try{
            extractUsername(token);
            return (!isTokenExpired(token));
        }catch (SignatureException | UsernameNotFoundException e) {
            return false;
        }
    }

    public String getTokenForServices(UUID transactionId){
        logger.info("Transaction ID: {}, Getting jwt", transactionId);
        if (tokenForServices==null || !isTokenForServicesValid(tokenForServices)){
            tokenForServices = generateTokenForServices();
        }
        logger.info("Transaction ID: {}, Jwt returned: {}", transactionId,  tokenForServices);
        return "Bearer "+tokenForServices;
    }

    private String generateTokenForServices() {
        Map<String, Object> claims = new HashMap<>();
        String token = createTokenForServices(claims);
        return token;
    }

    private String createTokenForServices(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setAudience("service").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private boolean isTokenForServicesValid(String token) {
        try{
            String audience = extractAudience(token);
            return (audience!=null && audience.equals(AUDIENCE_TYPE) && !isTokenExpired(token));
        }catch (SignatureException e){
            return false;
        }
    }

    public String extractUsernameOrReturnNull(String token){
        try{
            return extractUsername(token);
        }catch (Exception e) {
            return null;
        }
    }
}