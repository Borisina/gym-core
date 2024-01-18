package com.kolya.gym.controller;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Api(value = "API for trainings", tags = "Trainings")
@Controller
public class TrainingController {

    private final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TraineeService traineeService;


    @ApiOperation(value = "Create training", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED, Training created"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PostMapping("/trainings")
    public ResponseEntity<?> createTraining(@RequestBody TrainingData trainingData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, POST /trainings was called with body {}", transactionId, trainingData);
        try{
            Training training = trainingService.create(transactionId, trainingData);
            logger.info("Transaction ID: {}, 200 OK, Training was created {}", transactionId, training);
            return ResponseEntity.status(HttpStatus.CREATED).body(training);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Get trainings by trainee's username and criteria", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainings returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/trainees/{username}/trainings")
    public ResponseEntity<?> getByTraineeUsernameAndCriteria(@PathVariable("username") String username,@RequestBody TrainingCriteria trainingCriteria){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainees/{}/trainings was called with body {}", transactionId, username, trainingCriteria);
        try{
            List<TrainingData> trainings=null;
            if (trainingService.isEmptyCriteria(trainingCriteria)){
                trainings = trainingService.trainingListToTrainingDataList(traineeService.getTrainings(transactionId, username));
            }else{
                trainingCriteria.validate();
                trainings = trainingService.getByTraineeUsernameAndCriteria(transactionId, username,trainingCriteria);
            }
            logger.info("Transaction ID: {}, 200 OK, List of trainings was returned {}", transactionId, trainings);
            return ResponseEntity.status(HttpStatus.OK).body(trainings);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Get trainings by trainer's username and criteria", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainings returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/trainers/{username}/trainings")
    public ResponseEntity<?> getByTrainerUsernameAndCriteria(@PathVariable("username") String username,@RequestBody TrainingCriteria trainingCriteria){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainers/{}/trainings was called with body {}", transactionId, username, trainingCriteria);
        try{
            List<TrainingData> trainings= Collections.emptyList();;
            if (trainingService.isEmptyCriteria(trainingCriteria)){
                trainings = trainingService.trainingListToTrainingDataList(trainerService.getTrainings(transactionId, username));
            }else{
                trainingCriteria.validate();
                trainings = trainingService.getByTrainerUsernameAndCriteria(transactionId, username,trainingCriteria);
            }
            logger.info("Transaction ID: {}, 200 OK, List of trainings was returned {}", transactionId, trainings);
            return ResponseEntity.status(HttpStatus.OK).body(trainings);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Get trainings types", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Training types returned"),
    })
    @GetMapping("/trainings/training-types")
    public ResponseEntity<?> getTrainingTypes(){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainings/training-types was called with body", transactionId);
        List<TrainingType> trainingTypes = trainingService.getTrainingTypes(transactionId);
        logger.info("Transaction ID: {}, 200 OK, Training Types were returned", transactionId);
        return ResponseEntity.status(HttpStatus.OK).body(trainingTypes);
    }
}
