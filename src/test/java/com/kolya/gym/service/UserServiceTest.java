package com.kolya.gym.service;

import com.kolya.gym.dao.UserDao;
import com.kolya.gym.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setId(1L);
    }

    @Test
    public void testCreate() {
        when(userDao.create(any(User.class))).thenReturn(user);
        User result = userService.create("firstName", "lastName");
        assertEquals(1L, result.getId());
    }

    @Test
    public void testDelete() {
        when(userDao.delete(1L)).thenReturn(user);
        User result = userService.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdate() {
        when(userDao.get(1L)).thenReturn(user);
        when(userDao.update(any(User.class))).thenReturn(true);
        User result = userService.update("newFirstName", "newLastName", 1L);
        assertEquals("newFirstName", result.getFirstName());
        assertEquals("newLastName", result.getLastName());
    }
}