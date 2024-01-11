package com.kolya.gym.controller;

import com.kolya.gym.data.ChangePasswordData;
import com.kolya.gym.service.UserService;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private UserService userService = mock(UserService.class);;
    private Logger logger = mock(Logger.class);
    private AuthController authController = new AuthController(userService, logger);

    @Test
    public void testChangeLoginSuccess() {
        ChangePasswordData changePasswordData = new ChangePasswordData("user", "oldPass", "newPass");

        ResponseEntity<?> responseEntity = authController.changeLogin(changePasswordData);

        verify(userService, times(1)).changePassword(any(UUID.class), eq(changePasswordData));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeLoginFailureWhenUsernameNotFoundException() {
        ChangePasswordData changePasswordData = new ChangePasswordData("user", "oldPass", "newPass");

        doThrow(new UsernameNotFoundException("User not found"))
                .when(userService).validateChangePasswordData(any(ChangePasswordData.class));

        ResponseEntity<?> responseEntity = authController.changeLogin(changePasswordData);

        verify(userService, times(1)).validateChangePasswordData(any(ChangePasswordData.class));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeLoginFailureWhenIllegalArgumentException() {
        ChangePasswordData changePasswordData = new ChangePasswordData("user", "oldPass", "newPass");

        doThrow(new IllegalArgumentException("Invalid argument"))
                .when(userService).validateChangePasswordData(any(ChangePasswordData.class));

        ResponseEntity<?> responseEntity = authController.changeLogin(changePasswordData);

        verify(userService, times(1)).validateChangePasswordData(any(ChangePasswordData.class));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}