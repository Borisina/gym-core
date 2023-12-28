package com.kolya.gym.dao;

import com.kolya.gym.domain.Training;
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

public class TrainingDaoTest {

    @InjectMocks
    private TrainingDao trainingDao;

    @Mock
    private InMemoryStorage storage;

    private Training training;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        training = new Training();
        training.setId(1L);
    }

    @Test
    public void testCreate() {
        when(storage.getNewTrainingId()).thenReturn(1L);
        when(storage.getTrainings()).thenReturn(new HashMap<>());
        Training result = trainingDao.create(training);
        assertEquals(1L, result.getId());
        verify(storage, times(1)).getNewTrainingId();
    }

    @Test
    public void testDelete() {
        Map<Long, Training> trainings = new HashMap<>();
        trainings.put(1L, training);
        when(storage.getTrainings()).thenReturn(trainings);
        Training result = trainingDao.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdate() {
        Map<Long, Training> trainings = new HashMap<>();
        trainings.put(1L, training);
        when(storage.getTrainings()).thenReturn(trainings);
        assertTrue(trainingDao.update(training));
    }

    @Test
    public void testGet() {
        Map<Long, Training> trainings = new HashMap<>();
        trainings.put(1L, training);
        when(storage.getTrainings()).thenReturn(trainings);
        Training result = trainingDao.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAll() {
        Map<Long, Training> trainings = new HashMap<>();
        trainings.put(1L, training);
        when(storage.getTrainings()).thenReturn(trainings);
        assertEquals(1, trainingDao.getAll().size());
    }
}