package com.kolya.gym.service;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.UserData;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.AuthenticationException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepo userRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateUser() {
        UserData userData = new UserData();
        userData.setFirstName("John");
        userData.setLastName("Doe");
        User generatedUser = userService.generateUser(userData);
        assertNotNull(generatedUser);
        assertEquals("John", generatedUser.getFirstName());
        assertEquals("Doe", generatedUser.getLastName());
        assertTrue(generatedUser.isActive());
    }

    @Test
    public void testGenerateUserForUpdate() {
        UserData userData = new UserData();
        userData.setFirstName("John");
        userData.setLastName("Doe");
        User generatedUser = userService.generateUserForUpdate(userData);
        assertNotNull(generatedUser);
        assertEquals("John", generatedUser.getFirstName());
        assertEquals("Doe", generatedUser.getLastName());
    }

    @Test
    public void testGenerateUsername() {
        UserData userData = new UserData();
        userData.setFirstName("John");
        userData.setLastName("Doe");
        when(userRepo.countDuplicates("John", "Doe")).thenReturn(0L);
        String username = userService.generateUsername(userData.getFirstName(), userData.getLastName());
        assertEquals("John.Doe", username);
    }

    @Test
    public void testChange() {
        User user = new User();
        User updatedUser = new User();
        updatedUser.setFirstName("NewJohn");
        updatedUser.setLastName("NewDoe");
        userService.change(user, updatedUser);
        assertEquals("NewJohn", user.getFirstName());
        assertEquals("NewDoe", user.getLastName());
    }

    @Test
    public void testChangePassword() {
        User user = new User();
        String newPassword = "newPassword";
        userService.changePassword(user, newPassword);
        assertEquals("newPassword", user.getPassword());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void testAuthenticate() throws AuthenticationException {
        AuthData authData = new AuthData();
        authData.setUsername("username");
        authData.setPassword("password");
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        when(userRepo.findByUsernameAndPassword("username", "password")).thenReturn(user);
        User resultUser = userService.authenticate(authData);
        assertEquals(user, resultUser);
    }

    @Test(expected = AuthenticationException.class)
    public void testAuthenticateFail() throws AuthenticationException {
        AuthData authData = new AuthData();
        authData.setUsername("username");
        authData.setPassword("wrongpassword");
        when(userRepo.findByUsernameAndPassword("username", "wrongpassword")).thenReturn(null);
        userService.authenticate(authData);
    }

    @Test
    public void testChangeActiveStatus() {
        User user = new User();
        user.setActive(true);
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(user));
        boolean isActive = userService.changeActiveStatus(1L);
        assertFalse(isActive);
        verify(userRepo, times(1)).save(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeActiveStatusFail() {
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.empty());
        userService.changeActiveStatus(1L);
    }
}