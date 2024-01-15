package com.kolya.gym.controller;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.Training;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.TrainingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.UUID;

import static com.kolya.gym.prepareddata.PreparedData.trainingDataList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class TrainingControllerTest{
    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    private TrainingController trainingController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.trainingController = new TrainingController(trainingService, trainerService, traineeService);
    }

    @Test
    public void testCreateTrainingSuccess() {
        TrainingData trainingData = trainingDataList.get(0);
        when(trainingService.create(any(UUID.class), any(TrainingData.class))).thenReturn(new Training());
        ResponseEntity<?> response = trainingController.createTraining(trainingData);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetByTraineeUsernameAndCriteriaSuccess() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(trainingService.getByTraineeUsernameAndCriteria(any(UUID.class), anyString(), any(TrainingCriteria.class))).thenReturn(new ArrayList<TrainingData>());
        ResponseEntity<?> response = trainingController.getByTraineeUsernameAndCriteria("username", trainingCriteria);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetByTrainerUsernameAndCriteriaSuccess() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(trainingService.getByTrainerUsernameAndCriteria(any(UUID.class), anyString(), any(TrainingCriteria.class))).thenReturn(new ArrayList<TrainingData>());
        ResponseEntity<?> response = trainingController.getByTrainerUsernameAndCriteria("username", trainingCriteria);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testCreateTrainingFailure() {
        TrainingData trainingData = new TrainingData();
        when(trainingService.create(any(UUID.class), any(TrainingData.class))).thenThrow(IllegalArgumentException.class);
        ResponseEntity<?> response = trainingController.createTraining(trainingData);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetByTraineeUsernameAndCriteriaFailure() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(trainingService.getByTraineeUsernameAndCriteria(any(UUID.class), anyString(), any(TrainingCriteria.class))).thenThrow(IllegalArgumentException.class);
        ResponseEntity<?> response = trainingController.getByTraineeUsernameAndCriteria("username", trainingCriteria);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetByTrainerUsernameAndCriteriaFailure() {
        TrainingCriteria trainingCriteria = new TrainingCriteria();
        when(trainingService.getByTrainerUsernameAndCriteria(any(UUID.class), anyString(), any(TrainingCriteria.class))).thenThrow(IllegalArgumentException.class);
        ResponseEntity<?> response = trainingController.getByTrainerUsernameAndCriteria("username", trainingCriteria);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }
}