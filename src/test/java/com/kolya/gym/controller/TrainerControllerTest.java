package com.kolya.gym.controller;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kolya.gym.prepareddata.PreparedData.trainerDataList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;


    private TrainerController trainerController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.trainerController = new TrainerController(trainerService, userService);
    }

    @Test
    public void testCreateTrainerSuccess() {
        TrainerData trainerData = trainerDataList.get(0);
        AuthData authData = new AuthData("trainer", "pass");
        when(trainerService.create(any(UUID.class), any(TrainerData.class))).thenReturn(authData);

        ResponseEntity<?> response = trainerController.createTrainer(trainerData);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetTrainerByUsernameSuccess() {
        when(trainerService.getByUsername(any(UUID.class), anyString())).thenReturn(new Trainer());
        ResponseEntity<?> response = trainerController.getByUsername("username");

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTrainerSuccess() {
        TrainerData trainerData = trainerDataList.get(0);
        trainerData.setActive(true);
        when(trainerService.update(any(UUID.class), any(TrainerData.class), anyString())).thenReturn(new Trainer());

        ResponseEntity<?> response = trainerController.updateTrainer(trainerData, "username");

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetNotAssignedOnTraineeSuccess() {
        List<Trainer> trainerListMock = new ArrayList<>();
        when(trainerService.getNotAssignedOnTrainee(any(UUID.class), anyString())).thenReturn(trainerListMock);

        ResponseEntity<?> response = trainerController.getNotAssignedOnTrainee("username");

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testChangeActiveStatusSuccess() {
        when(trainerService.changeActiveStatus(any(UUID.class), anyString())).thenReturn(Boolean.TRUE);

        ResponseEntity<?> response = trainerController.changeActiveStatus("username");

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testCreateTrainerFailure() {
        TrainerData trainerData = new TrainerData();
        when(trainerService.create(any(UUID.class), any(TrainerData.class))).thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = trainerController.createTrainer(trainerData);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetTrainerByUsernameFailure() {
        when(trainerService.getByUsername(any(UUID.class), anyString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = trainerController.getByUsername("username");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTrainerFailure() {
        TrainerData trainerData = new TrainerData();
        when(trainerService.update(any(UUID.class), any(TrainerData.class), anyString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = trainerController.updateTrainer(trainerData, "username");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetNotAssignedOnTraineeFailure() {
        when(trainerService.getNotAssignedOnTrainee(any(UUID.class), anyString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = trainerController.getNotAssignedOnTrainee("username");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testChangeActiveStatusFailure() {
        when(trainerService.changeActiveStatus(any(UUID.class), anyString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> response = trainerController.changeActiveStatus("username");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }
}