package com.kolya.gym.dao;

import com.kolya.gym.domain.User;
import com.kolya.gym.storage.InMemoryStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    @InjectMocks
    private UserDao userDao;

    @Mock
    private InMemoryStorage storage;

    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setId(1L);
    }

    @Test
    public void testCreate() {
        when(storage.getNewUserId()).thenReturn(1L);
        when(storage.getUsers()).thenReturn(new HashMap<>());
        User result = userDao.create(user);
        assertEquals(1L, result.getId());
        verify(storage, times(1)).getNewUserId();
    }

    @Test
    public void testDelete() {
        Map<Long, User> users = new HashMap<>();
        users.put(1L, user);
        when(storage.getUsers()).thenReturn(users);
        User result = userDao.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdate() {
        Map<Long, User> users = new HashMap<>();
        users.put(1L, user);
        when(storage.getUsers()).thenReturn(users);
        assertTrue(userDao.update(user));
    }

    @Test
    public void testGet() {
        Map<Long, User> users = new HashMap<>();
        users.put(1L, user);
        when(storage.getUsers()).thenReturn(users);
        User result = userDao.get(1L);
        assertEquals(1L, result.getId());
    }
    @Test
    public void testGetAll() {
        Map<Long, User> users = new HashMap<>();
        users.put(1L, user);
        when(storage.getUsers()).thenReturn(users);
        assertEquals(1, userDao.getAll().size());
    }
}