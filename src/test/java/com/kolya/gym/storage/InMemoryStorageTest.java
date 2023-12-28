package com.kolya.gym.storage;

import com.kolya.gym.domain.User;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InMemoryStorageTest {

    @InjectMocks
    private InMemoryStorage inMemoryStorage;

    @Mock
    private Logger logger;

    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1L);
    }

    @Test
    public void testGetNewTraineeId() {
        ReflectionTestUtils.setField(inMemoryStorage, "traineeCounter", 5L);
        long id = inMemoryStorage.getNewTraineeId();
        assertEquals(6L, id);
    }

    @Test
    public void testGetNewTrainerId() {
        ReflectionTestUtils.setField(inMemoryStorage, "trainerCounter", 15L);
        long id = inMemoryStorage.getNewTrainerId();
        assertEquals(16L, id);
    }

    @Test
    public void testGetNewTrainingId() {
        ReflectionTestUtils.setField(inMemoryStorage, "trainingCounter", 20L);
        long id = inMemoryStorage.getNewTrainingId();
        assertEquals(21L, id);
    }

    @Test
    public void testGetNewUserId() {
        ReflectionTestUtils.setField(inMemoryStorage, "userCounter", 10L);
        long id = inMemoryStorage.getNewUserId();
        assertEquals(11L, id);
    }

    @Test
    public void testGetUsers() {
        Map<Long, User> users = inMemoryStorage.getUsers();
        assertEquals(0, users.size());
    }
}