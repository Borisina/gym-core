package com.kolya.gym.controller;

import com.kolya.gym.data.ChangePasswordData;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Api(value = "API for authentication", tags = "Auth")
@RestController
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
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
}
