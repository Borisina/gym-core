package com.kolya.gym.facade;

import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.Training;
import com.kolya.gym.service.TrainingService;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class TrainingFacadeTest {

    @InjectMocks
    private TrainingFacade trainingFacade;

    @Mock
    private TrainingService trainingService;

    @Mock
    private Logger logger;

    private Training training;
    private TrainingData trainingData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        training = new Training();
        training.setId(1L);
        trainingData = new TrainingData();
        trainingData.setTrainingName("boxing");
        trainingData.setTraineeId(1L);
        trainingData.setTrainerId(1L);
        trainingData.setDuration(60);
        trainingData.setTrainingType("type_1");
        trainingData.setTrainingDate(new Date());
    }

    @Test
    public void testCreateTraining() {
        when(trainingService.create(any(TrainingData.class))).thenReturn(training);
        Training result = trainingFacade.createTraining(trainingData);
        assertNotNull(result);
    }

    @Test
    public void testGetTraining() {
        when(trainingService.get(1L)).thenReturn(training);
        Training result = trainingFacade.getTraining(1L);
        assertNotNull(result);
    }

    @Test
    public void testGetAllTrainings() {
        when(trainingService.getAll()).thenReturn(Collections.singletonList(training));
        List<Training> result = trainingFacade.getAllTrainings();
        assertNotNull(result);
    }

    //Test for invalid data
    @Test
    public void testCreateTrainingWithInvalidData() {
        trainingData.setDuration(-60);
        Training result = trainingFacade.createTraining(trainingData);
        assertNull(result);
    }
}