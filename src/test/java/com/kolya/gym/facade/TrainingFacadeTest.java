package com.kolya.gym.facade;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.TrainingType;
import com.kolya.gym.service.TrainingService;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TrainingFacadeTest {

    @InjectMocks
    private TrainingFacade facade;

    @Mock
    private TrainingService trainingService;

    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTraining() {
        Training training = new Training();
        TrainingData trainingData = new TrainingData();
        trainingData.setTrainingDate(new Date());
        trainingData.setTrainingName("Name");
        trainingData.setTrainingType(TrainingType.TYPE_1);
        trainingData.setDuration(20);
        trainingData.setTrainerId(1L);
        trainingData.setTraineeId(1L);
        when(trainingService.create(trainingData)).thenReturn(training);

        Training result = facade.createTraining(trainingData);

        assertEquals(training, result);
    }

    @Test
    public void testGetTraining() {
        Training training = new Training();
        when(trainingService.get(1L)).thenReturn(training);

        Training result = facade.getTraining(1L);

        assertEquals(training, result);
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(trainingService.getAll()).thenReturn(trainings);

        List<Training> result = facade.getAllTrainings();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetByTraineeUsernameAndCriteria() {
        TrainingCriteria criteria = new TrainingCriteria();
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(trainingService.getByTraineeUsernameAndCriteria("test", criteria)).thenReturn(trainings);

        List<Training> result = facade.getByTraineeUsernameAndCriteria("test", criteria);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetByTrainerUsernameAndCriteria() {
        TrainingCriteria criteria = new TrainingCriteria();
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(trainingService.getByTrainerUsernameAndCriteria("test", criteria)).thenReturn(trainings);

        List<Training> result = facade.getByTrainerUsernameAndCriteria("test", criteria);

        assertEquals(2, result.size());
    }

    @Test
    public void createTraining_NullData_Test() {
        assertNull(facade.createTraining(null));
    }

    @Test
    public void getTraining_InvalidId_Test() {
        when(trainingService.get(-1L)).thenThrow(IllegalArgumentException.class);
        assertNull(facade.getTraining(-1));
    }

    @Test
    public void getByTraineeUsernameAndCriteria_NullUsername_Test() {
        TrainingCriteria mockCriteria = mock(TrainingCriteria.class);
        assertNull(facade.getByTraineeUsernameAndCriteria(null, mockCriteria));
    }

    @Test
    public void getByTraineeUsernameAndCriteria_InvalidCriteria_Test() {
        TrainingCriteria invalidCriteria = mock(TrainingCriteria.class);
        doThrow(IllegalArgumentException.class).when(invalidCriteria).validate();
        assertNull(facade.getByTraineeUsernameAndCriteria("username", invalidCriteria));
    }

    @Test
    public void getByTrainerUsernameAndCriteria_NullUsername_Test() {
        TrainingCriteria mockCriteria = mock(TrainingCriteria.class);
        assertNull(facade.getByTrainerUsernameAndCriteria(null, mockCriteria));
    }

    @Test
    public void getByTrainerUsernameAndCriteria_InvalidCriteria_Test() {
        TrainingCriteria invalidCriteria = mock(TrainingCriteria.class);
        doThrow(IllegalArgumentException.class).when(invalidCriteria).validate();
        assertNull(facade.getByTrainerUsernameAndCriteria("username", invalidCriteria));
    }
}