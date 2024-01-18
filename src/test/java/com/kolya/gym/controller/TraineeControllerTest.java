package com.kolya.gym.controller;

import com.kolya.gym.actuator.PrometheusMetrics;
import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.kolya.gym.prepareddata.PreparedData.traineeDataList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private UserService userService;

    @Mock
    private  PrometheusMetrics prometheusMetrics;

    @InjectMocks
    private TraineeController traineeController;

    @Test
    public void testCreateTrainee() {
        TraineeData traineeData = traineeDataList.get(0);

        AuthData authData = new AuthData("user", "pass");

        when(traineeService.create(any(UUID.class), any(TraineeData.class))).thenReturn(authData);

        ResponseEntity<?> response = traineeController.createTrainee(traineeData);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    public void testGetByUsername() {
        Trainee trainee = new Trainee();
        when(traineeService.getByUsername(any(UUID.class), anyString())).thenReturn(trainee);

        ResponseEntity<?> response = traineeController.getByUsername("username");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTrainee() {
        Trainee expectedTrainee = new Trainee();
        TraineeDataUpdate update = new TraineeDataUpdate();
        update.setUsername("username");
        update.setFirstName("first");
        update.setLastName("last");
        update.setActive(true);
        when(traineeService.update(any(UUID.class), any(TraineeDataUpdate.class))).thenReturn(expectedTrainee);

        ResponseEntity<?> responseEntity = traineeController.updateTrainee(update, "username");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testDeleteByUsername() {
        Trainee trainee = new Trainee();
        when(traineeService.deleteByUsername(any(UUID.class), anyString())).thenReturn(trainee);

        ResponseEntity<?> response = traineeController.deleteByUsername("username");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTrainersList() {
        List<String> trainerList = List.of("Trainer1","Trainer2");

        ResponseEntity<?> response = traineeController.updateTrainersList(trainerList, "username");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testChangeActiveStatus() {
        when(traineeService.changeActiveStatus(any(UUID.class), anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = traineeController.changeActiveStatus("username");

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testCreateTraineeFailure() {
        TraineeData traineeData = new TraineeData();

        ResponseEntity<?> response = traineeController.createTrainee(traineeData);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testGetByUsernameFailure() {
        when(traineeService.getByUsername(any(UUID.class), anyString()))
                .thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = traineeController.getByUsername("username");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTraineeFailure() {
        TraineeDataUpdate update = new TraineeDataUpdate();

        ResponseEntity<?> responseEntity = traineeController.updateTrainee(update, "username");

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testDeleteByUsernameFailure() {
        when(traineeService.deleteByUsername(any(UUID.class), anyString()))
                .thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = traineeController.deleteByUsername("username");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTrainersListFailure() {
        List<String> trainerList = List.of("Trainer1","Trainer2");

        when(traineeService.updateList(any(UUID.class), eq(trainerList), eq("username")))
                .thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = traineeController.updateTrainersList(trainerList, "username");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testChangeActiveStatusFailure() {
        when(traineeService.changeActiveStatus(any(UUID.class), eq("username")))
                .thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> responseEntity = traineeController.changeActiveStatus("username");

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

}