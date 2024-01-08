package com.kolya.gym.service;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import com.kolya.gym.repo.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrainingServiceTest {

    @InjectMocks
    TrainingService trainingService;

    @Mock
    TrainingRepo trainingRepo;

    @Mock
    TrainerRepo trainerRepo;

    @Mock
    TraineeRepo traineeRepo;

    @Mock
    UserRepo userRepo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTraining() throws Exception {
        TrainingData trainingData = new TrainingData();
        trainingData.setTrainerId(1L);
        trainingData.setTraineeId(1L);
        Training training = new Training();

        when(traineeRepo.findById(trainingData.getTraineeId())).thenReturn(java.util.Optional.of(new Trainee()));
        when(trainerRepo.findById(trainingData.getTrainerId())).thenReturn(java.util.Optional.of(new Trainer()));
        when(trainingRepo.save(any(Training.class))).thenReturn(training);

        Training trainingResult = trainingService.create(trainingData);

        assertNotNull(trainingResult);
    }

    @Test
    public void testGetTraining() throws Exception {
        Training training = new Training();
        training.setId(1L);

        when(trainingRepo.findById(training.getId())).thenReturn(java.util.Optional.of(training));

        Training trainingResult = trainingService.get(training.getId());

        assertNotNull(trainingResult);
    }

    @Test
    public void testGetByTraineeUsernameAndCriteria() throws Exception {
        String username = "kolya";
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        trainingCriteria.setTrainingType(TrainingType.TYPE_1);
        Training training = new Training();
        User user = new User();
        user.setId(1L);
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        List<Training> trainings = Arrays.asList(training);

        when(userRepo.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(traineeRepo.findByUserId(1L)).thenReturn(java.util.Optional.of(trainee));
        when(trainingRepo.findByTraineeIdAndCriteria(
                1L,
                trainingCriteria.getTrainingType().name(),
                trainingCriteria.getTrainingName(),
                trainingCriteria.getTrainingDateFrom(),
                trainingCriteria.getTrainingDateTo(),
                trainingCriteria.getDurationMin(),
                trainingCriteria.getDurationMax())).thenReturn(trainings);

        List<Training> result = trainingService.getByTraineeUsernameAndCriteria(username, trainingCriteria);

        assertNotNull(result);
    }

    @Test
    public void testGetByTrainerUsernameAndCriteria() throws Exception {
        String username = "kolya";
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        trainingCriteria.setTrainingType(TrainingType.TYPE_1);
        User user = new User();
        user.setId(1L);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        Training training = new Training();
        List<Training> trainings = Arrays.asList(training);

        when(userRepo.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(trainerRepo.findByUserId(1L)).thenReturn(java.util.Optional.of(trainer));
        when(trainingRepo.findByTrainerIdAndCriteria(
                1L,
                trainingCriteria.getTrainingType().name(),
                trainingCriteria.getTrainingName(),
                trainingCriteria.getTrainingDateFrom(),
                trainingCriteria.getTrainingDateTo(),
                trainingCriteria.getDurationMin(),
                trainingCriteria.getDurationMax())).thenReturn(trainings);

        List<Training> result = trainingService.getByTrainerUsernameAndCriteria(username, trainingCriteria);

        assertNotNull(result);
    }

    @Test
    public void testGetAll() throws Exception {
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> trainings = Arrays.asList(training1, training2);

        when(trainingRepo.findAll()).thenReturn(trainings);

        List<Training> result = trainingService.getAll();

        assertNotNull(result);
    }
}