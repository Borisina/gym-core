package com.kolya.gym.dao;

import com.kolya.gym.domain.Trainee;
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

public class TraineeDaoTest {

    @InjectMocks
    private TraineeDao traineeDao;

    @Mock
    private InMemoryStorage storage;

    private Trainee trainee;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        trainee = new Trainee();
        trainee.setId(1L);
    }

    @Test
    public void testCreate() {
        when(storage.getNewTraineeId()).thenReturn(1L);
        when(storage.getTrainees()).thenReturn(new HashMap<>());
        Trainee result = traineeDao.create(trainee);
        assertEquals(1L, result.getId());
        verify(storage, times(1)).getNewTraineeId();
    }

    @Test
    public void testDelete() {
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(1L, trainee);
        when(storage.getTrainees()).thenReturn(trainees);
        Trainee result = traineeDao.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdate() {
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(1L, trainee);
        when(storage.getTrainees()).thenReturn(trainees);
        assertTrue(traineeDao.update(trainee));
    }

    @Test
    public void testGet() {
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(1L, trainee);
        when(storage.getTrainees()).thenReturn(trainees);
        Trainee result = traineeDao.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAll() {
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(1L, trainee);
        when(storage.getTrainees()).thenReturn(trainees);
        assertEquals(1, traineeDao.getAll().size());
    }
}