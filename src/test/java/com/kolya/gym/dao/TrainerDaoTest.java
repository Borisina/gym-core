package com.kolya.gym.dao;

import com.kolya.gym.domain.Trainer;
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

public class TrainerDaoTest {

    @InjectMocks
    private TrainerDao trainerDao;

    @Mock
    private InMemoryStorage storage;

    private Trainer trainer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trainer = new Trainer();
        trainer.setId(1L);
    }

    @Test
    public void testCreate() {
        when(storage.getNewTrainerId()).thenReturn(1L);
        when(storage.getTrainers()).thenReturn(new HashMap<>());
        Trainer result = trainerDao.create(trainer);
        assertEquals(1L, result.getId());
        verify(storage, times(1)).getNewTrainerId();
    }

    @Test
    public void testDelete() {
        Map<Long, Trainer> trainers = new HashMap<>();
        trainers.put(1L, trainer);
        when(storage.getTrainers()).thenReturn(trainers);
        Trainer result = trainerDao.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdate() {
        Map<Long, Trainer> trainers = new HashMap<>();
        trainers.put(1L, trainer);
        when(storage.getTrainers()).thenReturn(trainers);
        assertTrue(trainerDao.update(trainer));
    }

    @Test
    public void testGet() {
        Map<Long, Trainer> trainers = new HashMap<>();
        trainers.put(1L, trainer);
        when(storage.getTrainers()).thenReturn(trainers);
        Trainer result = trainerDao.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAll() {
        Map<Long, Trainer> trainers = new HashMap<>();
        trainers.put(1L, trainer);
        when(storage.getTrainers()).thenReturn(trainers);
        assertEquals(1, trainerDao.getAll().size());
    }
}