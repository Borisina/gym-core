package com.kolya.gym.service;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TraineeServiceTest {
    @Mock
    private TraineeRepo traineeRepo;

    @Mock
    private TrainerRepo trainerRepo;

    @Mock
    private UserService userService;
    @InjectMocks
    private TraineeService traineeService;


    @Test
    public void testCreateTraineeSuccess() {
        TraineeData traineeData = new TraineeData();
        Trainee trainee = new Trainee();
        when(traineeRepo.save(any(Trainee.class))).thenReturn(trainee);
        when(userService.generateUserForCreate(any(UUID.class),eq(traineeData))).thenReturn(new User());
        AuthData authData = traineeService.create(UUID.randomUUID(), traineeData);
        assertNotNull(authData);
    }

    @Test
    public void testUpdateTraineeSuccess() {
        TraineeDataUpdate traineeData = new TraineeDataUpdate();
        traineeData.setUsername("username");
        when(traineeRepo.findByUserUsername("username")).thenReturn(Optional.of(new Trainee()));
        Trainee trainee = traineeService.update(UUID.randomUUID(), traineeData);
        assertNotNull(trainee);
    }

    @Test
    public void testGetByUsernameSuccess() {
        when(traineeRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainee()));
        Trainee trainee = traineeService.getByUsername(UUID.randomUUID(), "username");
        assertNotNull(trainee);

    }

    @Test
    public void testChangeActiveStatusSuccess() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setActive(true);
        trainee.setUser(user);
        when(traineeRepo.findByUserUsername(anyString())).thenReturn(Optional.of(trainee));
        boolean isActive = traineeService.changeActiveStatus(UUID.randomUUID(), "username");
        assertFalse(isActive);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateTraineeFailure() {
        TraineeDataUpdate traineeData = new TraineeDataUpdate();
        traineeService.update(UUID.randomUUID(), traineeData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByUsernameFailure() {
        when(traineeRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        traineeService.getByUsername(UUID.randomUUID(), "username");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeActiveStatusFailure() {
        when(traineeRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        traineeService.changeActiveStatus(UUID.randomUUID(), "username");
    }


    @Test
    public void testUpdateListSuccess() {
        when(traineeRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainee()));
        when(trainerRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainer()));
        List<String> trainersUsernamesList = new ArrayList<>();
        trainersUsernamesList.add("trainerUsername");
        Set<Trainer> trainerList = traineeService.updateList(UUID.randomUUID(), trainersUsernamesList, "username");
        assertNotNull(trainerList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateListFailure() {
        when(traineeRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        List<String> trainersUsernamesList = new ArrayList<>();
        trainersUsernamesList.add("trainerUsername");
        traineeService.updateList(UUID.randomUUID(), trainersUsernamesList, "username");
    }
/*
    @Test
    public void testDeleteByUsernameSuccess() {
        when(traineeRepo.deleteByUsername(anyString())).thenReturn();
        Trainee trainee = traineeService.deleteByUsername(UUID.randomUUID(), "username");
        assertNotNull(trainee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByUsernameFailure() {
        when(traineeRepo.deleteByUsername(anyString())).thenThrow(IllegalArgumentException.class);
        traineeService.deleteByUsername(UUID.randomUUID(), "username");
    }
*/
    @Test
    public void testGetTrainingsSuccess() {
        Trainee trainee = new Trainee();
        trainee.setTrainingsList(new ArrayList<>());
        when(traineeRepo.findByUserUsername(anyString())).thenReturn(Optional.of(trainee));
        List<Training> trainingList = traineeService.getTrainings(UUID.randomUUID(), "username");
        assertNotNull(trainingList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTrainingsFailure() {
        when(traineeRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        traineeService.getTrainings(UUID.randomUUID(), "username");
    }
}