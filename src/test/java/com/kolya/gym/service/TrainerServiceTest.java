package com.kolya.gym.service;

import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class TrainerServiceTest {

    @InjectMocks
    TrainerService trainerService;

    @Mock
    TrainerRepo trainerRepo;

    @Mock
    UserService userService;

    @Mock
    UserRepo userRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainer() {
        TrainerData trainerData = new TrainerData();
        trainerData.setSpecialization("Yoga");
        Trainer trainer = new Trainer();
        trainer.setSpecialization("Yoga");
        User user = new User();
        trainer.setUser(user);

        when(userService.generateUser(trainerData)).thenReturn(user);
        when(trainerRepo.save(any(Trainer.class))).thenReturn(trainer);

        Trainer resultTrainer = trainerService.create(trainerData);

        assertNotNull(resultTrainer);
        assertEquals(resultTrainer.getSpecialization(), trainerData.getSpecialization());
    }

    @Test
    public void testUpdateTrainer() {
        TrainerData trainerData = new TrainerData();
        trainerData.setSpecialization("Boxing");
        Trainer trainer = new Trainer();
        trainer.setSpecialization("Boxing");
        User user = new User();
        trainer.setUser(user);
        when(userService.generateUserForUpdate(trainerData)).thenReturn(user);
        when(trainerRepo.findById(any(Long.class))).thenReturn(java.util.Optional.of(trainer));
        when(trainerRepo.save(any(Trainer.class))).thenReturn(trainer);

        Trainer resultTrainer = trainerService.update(trainerData,1 );

        assertNotNull(resultTrainer);
        assertEquals("Boxing", resultTrainer.getSpecialization());
    }

    @Test
    public void testGetTrainer() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);

        when(trainerRepo.findById(trainer.getId())).thenReturn(java.util.Optional.of(trainer));

        Trainer resultTrainer = trainerService.get(trainer.getId());

        assertNotNull(resultTrainer);
        assertEquals(trainer.getId(), resultTrainer.getId());
    }

    @Test
    public void testGetByUsername() {
        String username = "testUsername";
        User user = new User();
        user.setUsername(username);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        when(userRepo.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(trainerRepo.findByUserId(user.getId())).thenReturn(java.util.Optional.of(trainer));
        Trainer result = trainerService.getByUsername(username);
        assertEquals(username, result.getUser().getUsername());
    }

    @Test
    public void testGetAllTrainers() {
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);
        when(trainerRepo.findAll()).thenReturn(trainers);
        List<Trainer> result = trainerService.getAll();
        assertEquals(trainers.size(), result.size());
    }
}