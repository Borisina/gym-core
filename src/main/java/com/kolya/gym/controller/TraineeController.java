package com.kolya.gym.controller;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Api(value = "API for trainees", tags = "Trainees")
@RequestMapping("/trainees")
@RestController
public class TraineeController {
    private final TraineeService traineeService;
    private final UserService userService;

    private final Logger logger;


    @Autowired
    public TraineeController(TraineeService traineeService, UserService userService, Logger logger) {
        this.traineeService = traineeService;
        this.userService = userService;
        this.logger = logger;
    }

    @ApiOperation(value = "Create trainee", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED, Trainee created"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PostMapping
    public ResponseEntity<?> createTrainee(@RequestBody TraineeData traineeData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, POST /trainees was called with body {}",transactionId,  traineeData);
        try{
            traineeData.validate();
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        AuthData authData = traineeService.create(transactionId, traineeData);
        logger.info("Transaction ID: {}, 201 CREATED, Trainee {}, was created.", transactionId, authData.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(authData);
    }

    @ApiOperation(value = "Get trainee", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainee returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainees/{} was called", transactionId, username);
        try{
            userService.validateUsername(username);
            Trainee trainee = traineeService.getByUsername(transactionId, username);
            logger.info("Transaction ID: {}, 200 OK, Trainee was returned: {}",transactionId , trainee);
            return ResponseEntity.status(HttpStatus.OK).body(trainee);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @ApiOperation(value = "Update trainee", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainee updated"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PutMapping("/{username}")
    public ResponseEntity<?> updateTrainee(@RequestBody TraineeDataUpdate traineeDataUpdate, @PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, PUT /trainees/{} was called with body {}",transactionId, username,traineeDataUpdate);
        try{
            userService.validateUsername(username);
            traineeDataUpdate.setUsername(username);
            traineeDataUpdate.validate();
            Trainee trainee = traineeService.update(transactionId, traineeDataUpdate);
            logger.info("Transaction ID: {}, 200 OK, Trainee was updated: {}", transactionId, trainee);
            return ResponseEntity.status(HttpStatus.OK).body(trainee);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @ApiOperation(value = "Delete trainee", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainee deleted"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteByUsername(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, DELETE /trainees/{} was called", transactionId, username);
        try{
            userService.validateUsername(username);
            Trainee trainee = traineeService.deleteByUsername(transactionId, username);
            logger.info("Transaction ID: {}, 200 OK, Trainee was deleted: {}", transactionId, trainee);
            return ResponseEntity.status(HttpStatus.OK).body("Trainee deleted.");
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Update trainee's list of trainers", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainers list updated"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PutMapping("/{username}/trainers")
    public ResponseEntity<?> updateTrainersList(@RequestBody List<String> trainersList, @PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, PUT /trainees/{}/trainers was called with body {}", transactionId, username, trainersList);
        try{
            userService.validateUsername(username);
            List<Trainer> trainers = traineeService.updateList(transactionId, trainersList,username);
            logger.info("Transaction ID: {}, 200 OK, Trainee's traineersList was updated {}", transactionId, trainers);
            return ResponseEntity.status(HttpStatus.OK).body(trainers);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Switch trainee's active status", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainee's active status switched"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PatchMapping("/{username}/switch-active-status")
    public ResponseEntity<?> changeActiveStatus(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, PATCH /trainees/{}/switch-active-status was called", transactionId, username);
        try{
            userService.validateUsername(username);
            boolean status = traineeService.changeActiveStatus(transactionId, username);
            logger.info("Transaction ID: {}, 200 OK, Trainee's active status was switched to {}", transactionId, status);
            return ResponseEntity.status(HttpStatus.OK).body("Active status switched to "+status);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
