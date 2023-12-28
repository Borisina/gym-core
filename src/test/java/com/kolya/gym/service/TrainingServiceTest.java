package com.kolya.gym.service;

import com.kolya.gym.dao.TraineeDao;
import com.kolya.gym.dao.TrainerDao;
import com.kolya.gym.dao.TrainingDao;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.TrainingType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    private Training training;
    private TrainingData trainingData;
    private Trainer trainer;
    private Trainee trainee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        training = new Training();
        training.setId(1L);

        trainingData = new TrainingData();
        trainingData.setTrainingType("type_1");

        trainer = new Trainer();
        trainer.setId(1L);

        trainee = new Trainee();
        trainee.setId(2L);
    }

    @Test
    public void testCreate() {
        when(trainerDao.get(any(Long.class))).thenReturn(trainer);
        when(traineeDao.get(any(Long.class))).thenReturn(trainee);
        when(trainingDao.create(any(Training.class))).thenReturn(training);
        Training result = trainingService.create(trainingData);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGet() {
        when(trainingDao.get(1L)).thenReturn(training);
        Training result = trainingService.get(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetAll() {
        when(trainingDao.getAll()).thenReturn(Collections.singletonList(training));
        List<Training> result = trainingService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
    }
}