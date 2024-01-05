package com.kolya.gym.service;

import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TraineeServiceTest {

    @InjectMocks
    TraineeService traineeService;

    @Mock
    TraineeRepo traineeRepo;

    @Mock
    UserService userService;

    @Mock
    UserRepo userRepo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        TraineeData traineeData = new TraineeData();
        traineeData.setAddress("Address");
        traineeData.setDateOfBirth(new Date());

        Trainee mockTrainee = new Trainee();
        mockTrainee.setAddress(traineeData.getAddress());
        mockTrainee.setDateOfBirth(traineeData.getDateOfBirth());

        when(userService.generateUser(traineeData)).thenReturn(new User());
        when(traineeRepo.save(any(Trainee.class))).thenReturn(mockTrainee);

        Trainee trainee = traineeService.create(traineeData);

        assertNotNull(trainee);
        assertEquals("Address", trainee.getAddress());
    }

    @Test
    public void testUpdateTrainee() {
        TraineeDataUpdate traineeDataUpdate = new TraineeDataUpdate();
        traineeDataUpdate.setId(1L);
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        when(traineeRepo.findById(trainee.getId())).thenReturn(java.util.Optional.of(trainee));
        when(traineeRepo.save(trainee)).thenReturn(trainee);
        Trainee updatedTrainee = traineeService.update(traineeDataUpdate);
        assertNotNull(updatedTrainee);
    }

    @Test
    public void testUpdateTraineeList() {
        TraineeDataUpdate traineeDataUpdate1 = new TraineeDataUpdate();
        traineeDataUpdate1.setId(1);
        TraineeDataUpdate traineeDataUpdate2 = new TraineeDataUpdate();
        traineeDataUpdate2.setId(2);
        List<TraineeDataUpdate> dataUpdateList = Arrays.asList(traineeDataUpdate1, traineeDataUpdate2);
        Trainee trainee1 = new Trainee();
        trainee1.setId(1L);
        Trainee trainee2 = new Trainee();
        trainee2.setId(2L);
        when(traineeRepo.findById(trainee1.getId())).thenReturn(java.util.Optional.of(trainee1));
        when(traineeRepo.save(trainee1)).thenReturn(trainee1);
        when(traineeRepo.findById(trainee2.getId())).thenReturn(java.util.Optional.of(trainee2));
        when(traineeRepo.save(trainee2)).thenReturn(trainee2);
        List<Trainee> updatedTrainees = traineeService.updateList(dataUpdateList);
        assertEquals(2, updatedTrainees.size());
    }

    @Test
    public void testGetAllTrainees() {
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        List<Trainee> trainees = Arrays.asList(trainee1, trainee2);
        when(traineeRepo.findAll()).thenReturn(trainees);
        List<Trainee> result = traineeService.getAll();
        assertEquals(trainees.size(), result.size());
    }


    @Test
    public void testGetByUsername() {
        String username = "testUsername";
        User user = new User();
        user.setUsername(username);
        Trainee trainee = new Trainee();
        when(userRepo.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(traineeRepo.findByUserId(user.getId())).thenReturn(java.util.Optional.of(trainee));
        Trainee result = traineeService.getByUsername(username);
        assertEquals(trainee, result);
    }

    @Test
    public void testDeleteByUsername() {
        String username = "testUsername";
        User user = new User();
        user.setUsername(username);
        Trainee trainee = new Trainee();
        when(userRepo.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(traineeRepo.deleteByUserId(user.getId())).thenReturn(trainee);
        Trainee result = traineeService.deleteByUsername(username);
        assertEquals(trainee, result);
    }

    @Test
    public void testGetNotAssigned() {
        Trainee trainee1 = new Trainee();
        Trainee trainee2 = new Trainee();
        List<Trainee> notAssignedTrainees = Arrays.asList(trainee1, trainee2);
        when(traineeRepo.getNotAssigned()).thenReturn(notAssignedTrainees);
        List<Trainee> result = traineeService.getNotAssigned();
        assertEquals(notAssignedTrainees.size(), result.size());
    }
}