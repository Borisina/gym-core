package com.kolya.gym.service;

import com.kolya.gym.dao.TraineeDao;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;


public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeDao traineeDao;

    private Trainee trainee;
    private TraineeData traineeData;
    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trainee = new Trainee();
        trainee.setId(1L);

        traineeData = new TraineeData();

        user = new User();
        user.setId(1L);
    }

    @Test
    public void testCreateTrainee() {
        when(traineeDao.create(any(Trainee.class))).thenReturn(trainee);
        Trainee result = traineeService.create(traineeData, user);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdateTrainee() {
        when(traineeDao.get(1L)).thenReturn(trainee);
        when(traineeDao.update(any(Trainee.class))).thenReturn(true);
        Trainee result = traineeService.update(traineeData, user, 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetTrainee() {
        when(traineeDao.get(1L)).thenReturn(trainee);
        Trainee result = traineeService.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testDeleteTrainee() {
        when(traineeDao.delete(1L)).thenReturn(trainee);
        Trainee result = traineeService.delete(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAllTrainees() {
        when(traineeDao.getAll()).thenReturn(Collections.singletonList(trainee));
        List<Trainee> result = traineeService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
    }
}