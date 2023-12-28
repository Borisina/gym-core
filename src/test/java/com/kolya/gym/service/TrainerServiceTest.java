package com.kolya.gym.service;

import com.kolya.gym.dao.TrainerDao;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerDao trainerDao;

    private Trainer trainer;
    private TrainerData trainerData;
    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trainer = new Trainer();
        trainer.setId(1L);

        trainerData = new TrainerData();
        user = new User();
        user.setId(1L);
    }

    @Test
    public void testCreate() {
        when(trainerDao.create(any(Trainer.class))).thenReturn(trainer);
        Trainer result = trainerService.create(trainerData, user);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdate() {
        when(trainerDao.get(1L)).thenReturn(trainer);
        when(trainerDao.update(any(Trainer.class))).thenReturn(true);
        Trainer result = trainerService.update(trainerData, user, 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGet() {
        when(trainerDao.get(1L)).thenReturn(trainer);
        Trainer result = trainerService.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testDelete() {
        when(trainerDao.delete(1L)).thenReturn(trainer);
        Trainer result = trainerService.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAll() {
        when(trainerDao.getAll()).thenReturn(Collections.singletonList(trainer));
        List<Trainer> result = trainerService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
    }
}