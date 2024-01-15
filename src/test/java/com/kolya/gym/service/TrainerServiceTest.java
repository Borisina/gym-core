package com.kolya.gym.service;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TrainerRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kolya.gym.prepareddata.PreparedData.trainerDataList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TrainerServiceTest {
    @Mock
    private TrainerRepo trainerRepo;

    @Mock
    private UserService userService;

    private TrainerService trainerService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.trainerService = new TrainerService(trainerRepo, userService);
    }

    @Test
    public void testCreateTrainerSuccess() {
        TrainerData trainerData = trainerDataList.get(0);
        when(trainerRepo.save(any(Trainer.class))).thenReturn(new Trainer());
        when(userService.generateUserForCreate(any(UUID.class),eq(trainerData))).thenReturn(new User());
        AuthData authData = trainerService.create(UUID.randomUUID(), trainerData);
        assertNotNull(authData);
    }

    @Test
    public void testGetByUsernameSuccess() {
        when(trainerRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainer()));
        Trainer trainer = trainerService.getByUsername(UUID.randomUUID(), "username");
        assertNotNull(trainer);
    }


    @Test
    public void testUpdateTrainerSuccess() {
        TrainerData trainerData = trainerDataList.get(0);
        trainerData.setActive(true);
        when(trainerRepo.save(any(Trainer.class))).thenReturn(new Trainer());
        when(trainerRepo.findByUserUsername("username")).thenReturn(Optional.of(new Trainer()));
        Trainer trainer = trainerService.update(UUID.randomUUID(), trainerData, "username");
        assertNotNull(trainer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByUsernameFailure() {
        when(trainerRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        trainerService.getByUsername(UUID.randomUUID(), "username");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateTrainerFailure() {
        TrainerData trainerData = new TrainerData();
        when(trainerRepo.save(any(Trainer.class))).thenThrow(IllegalArgumentException.class);
        trainerService.update(UUID.randomUUID(), trainerData, "username");
    }

    @Test
    public void testChangeActiveStatusSuccess() {
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setActive(true);
        when(trainerRepo.findByUserUsername(anyString())).thenReturn(Optional.of(trainer));
        boolean isActive = trainerService.changeActiveStatus(UUID.randomUUID(), "username");
        assertFalse(isActive);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeActiveStatusFailure() {
        when(trainerRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        trainerService.changeActiveStatus(UUID.randomUUID(), "username");
    }

    @Test
    public void testGetNotAssignedOnTraineeSuccess() {
        when(trainerRepo.findByUserUsername(anyString())).thenReturn(Optional.of(new Trainer()));
        List<Trainer> trainerList = trainerService.getNotAssignedOnTrainee(UUID.randomUUID(), "username");
        assertNotNull(trainerList);
    }

    /*@Test(expected = IllegalArgumentException.class)
    public void testGetNotAssignedOnTraineeFailure() {
        when(trainerRepo.findByUserUsername(anyString())).thenThrow(IllegalArgumentException.class);
        trainerService.getNotAssignedOnTrainee(UUID.randomUUID(), "username");
    }*/
}