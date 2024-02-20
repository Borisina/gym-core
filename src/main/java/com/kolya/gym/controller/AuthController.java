package com.kolya.gym.controller;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.ChangePasswordData;
import com.kolya.gym.exception.ExcessiveAttemptsException;
import com.kolya.gym.service.JwtService;
import com.kolya.gym.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Api(value = "API for authentication", tags = "Auth")
@RestController
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @ApiOperation(value = "Login", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, successful login, jwt-cookie set"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
            @ApiResponse(code = 403, message = "FORBIDDEN, Too many login attempts"),
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthData authData, HttpServletResponse response) throws Exception {
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {} POST /login was called for {}", transactionId, authData.getUsername());
        UserDetails userDetails = null;
        try {
            userService.validateUsername(authData.getUsername());
            userDetails = userService.authorize(authData);
        } catch (IllegalArgumentException  | UsernameNotFoundException e) {
            logger.info("Transaction ID: {} 400 BAD_REQUEST, Wrong username or password.", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong username or password.");
        }catch (ExcessiveAttemptsException e) {
            logger.info("Transaction ID: {} 403 FORBIDDEN, User {}: {}", transactionId, authData.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        String jwt = jwtService.generateToken(transactionId, userDetails);
        response.addCookie(getJwtCookie(jwt));
        logger.info("Transaction ID: {} 200 OK, Successful login for user {}", transactionId, authData.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }

    @ApiOperation(value = "Logout", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, successful logout, jwt-cookie expired"),
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) throws Exception {
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {} POST /logout was called", transactionId);
        response.addCookie(getExpiredJwtCookie());
        logger.info("Transaction ID: {} 200 OK, Successful logout", transactionId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @ApiOperation(value = "Change user's login", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, login changed successfully"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PutMapping("/change-login")
    public ResponseEntity<?> changeLogin(@RequestBody ChangePasswordData changePasswordData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {} PUT /change-login was called for {}", transactionId, changePasswordData.getUsername());
        try{
            userService.validateChangePasswordData(changePasswordData);
            userService.changePassword(transactionId, changePasswordData);
            logger.info("Transaction ID: {}, 200 OK, Password changed for user {}.",transactionId, changePasswordData.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body("Password changed.");
        }catch (UsernameNotFoundException | IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private Cookie getJwtCookie(String jwt){
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setMaxAge(24 * 60 * 60);  // установить срок действия куки 24 часа
        jwtCookie.setHttpOnly(true);  // куки доступны только на сервере, а не через JavaScript
        jwtCookie.setSecure(true);  // куки должны передаваться только по HTTPS
        jwtCookie.setPath("/");  // куки доступны для все сайта
        return jwtCookie;
    }

    private Cookie getExpiredJwtCookie(){
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        return jwtCookie;
    }
}
