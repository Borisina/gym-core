package com.kolya.gym.service;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.TrainingRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kolya.gym.prepareddata.PreparedData.trainingDataList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TrainingServiceTest {
    @Mock
    private TrainingRepo trainingRepo;

    @Mock
    private TrainerRepo trainerRepo;

    @Mock
    private TraineeRepo traineeRepo;

    private TrainingService trainingService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.trainingService = new TrainingService(trainingRepo, trainerRepo, traineeRepo);
    }

    @Test
    public void testCreateTrainingSuccess() {
        TrainingData trainingData = trainingDataList.get(0);
        Trainer trainer = new Trainer();
        trainer.setTraineesList(new ArrayList<>());
        Trainee trainee = new Trainee();
        trainee.setTrainersList(new ArrayList<>());
        when(trainingRepo.save(any(Training.class))).thenReturn(new Training());
        when(trainerRepo.findByUserUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(traineeRepo.findByUserUsername(any(String.class))).thenReturn(Optional.of(trainee));
        Training training = trainingService.create(UUID.randomUUID(), trainingData);
        assertNotNull(training);
    }

    @Test
    public void testGetByTraineeUsernameAndCriteriaSuccess() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(traineeRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainee()));
        when(trainingRepo.findByTraineeIdAndCriteria(anyLong(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        List<TrainingData> trainingData = trainingService.getByTraineeUsernameAndCriteria(UUID.randomUUID(), "username", trainingCriteria);
        assertNotNull(trainingData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByTraineeUsernameAndCriteriaFailure() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(traineeRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        trainingService.getByTraineeUsernameAndCriteria(UUID.randomUUID(), "username", trainingCriteria);
    }

    @Test
    public void testGetByTrainerUsernameAndCriteriaSuccess() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(trainerRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainer()));
        when(trainingRepo.findByTrainerIdAndCriteria(anyLong(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        List<TrainingData> trainingData = trainingService.getByTrainerUsernameAndCriteria(UUID.randomUUID(), "username", trainingCriteria);
        assertNotNull(trainingData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByTrainerUsernameAndCriteriaFailure() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(trainerRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        trainingService.getByTrainerUsernameAndCriteria(UUID.randomUUID(), "username", trainingCriteria);
    }

    @Test
    public void testIsEmptyCriteria() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        boolean isEmpty = trainingService.isEmptyCriteria(trainingCriteria);
        assertTrue(isEmpty);
    }
}