package com.kolya.gym.service;

import com.kolya.gym.data.ChangePasswordData;
import com.kolya.gym.data.UserData;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kolya.gym.prepareddata.PreparedData.trainingDataList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.userService = new UserService(userRepo, passwordEncoder);
    }

    @Test
    public void testGenerateUserForCreateSuccess() {
        UserData userData = new UserData();
        when(userRepo.countDuplicates(anyString(), anyString())).thenReturn(0L);
        User user = userService.generateUserForCreate(UUID.randomUUID(), userData);
        assertNotNull(user);
    }

    @Test
    public void testGenerateUserForUpdateSuccess() {
        UserData userData = new UserData();
        userData.setActive(true);
        User user = userService.generateUserForUpdate(UUID.randomUUID(), userData);
        assertNotNull(user);
    }

    @Test
    public void testGenerateUsernameSuccess() {
        when(userRepo.countDuplicates(anyString(), anyString())).thenReturn(0L);
        String username = userService.generateUsername(UUID.randomUUID(), "firstname", "lastname");
        assertNotNull(username);
    }

    @Test
    public void testChangePasswordSuccess() {
        ChangePasswordData changePasswordData = new ChangePasswordData();
        changePasswordData.setOldPassword("oldPassword");
        changePasswordData.setNewPassword("newPassword");
        changePasswordData.setUsername("username");
        User user = new User();
        user.setPassword("Hello");
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.changePassword(UUID.randomUUID(), changePasswordData);
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testChangePasswordUserNotFoundFailure() {
        ChangePasswordData changePasswordData = new ChangePasswordData();
        changePasswordData.setOldPassword("oldPassword");
        changePasswordData.setNewPassword("newPassword");
        changePasswordData.setUsername("username");

        userService.changePassword(UUID.randomUUID(), changePasswordData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordWrongOldPasswordFailure() {
        ChangePasswordData changePasswordData = new ChangePasswordData();
        changePasswordData.setOldPassword("oldPassword");
        changePasswordData.setNewPassword("newPassword");
        changePasswordData.setUsername("username");

        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        userService.changePassword(UUID.randomUUID(), changePasswordData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUsernameFailure() {
        userService.validateUsername("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatePasswordFailure() {
        userService.validatePassword("");
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        UserDetails userDetails = userService.loadUserByUsername("username");
        assertNotNull(userDetails);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameFailure() {
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
        userService.loadUserByUsername("username");
    }
}