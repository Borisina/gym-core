package com.kolya.gym.controller;

import com.kolya.gym.actuator.PrometheusMetrics;
import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Api(value = "API for trainers", tags = "Trainers")
@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @Autowired
    private TrainerService trainerService;
    @Autowired
    private UserService userService;
    @Autowired
    private PrometheusMetrics prometheusMetrics;

    @ApiOperation(value = "Create trainer", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED, Trainer created"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PostMapping
    public ResponseEntity<?> createTrainer(@RequestBody TrainerData trainerData){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, POST /trainers was called with body {}", transactionId, trainerData);
        prometheusMetrics.incrementCreateTrainerCounter();
        try{
            trainerData.validate();
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        AuthData authData = trainerService.create(transactionId, trainerData);
        logger.info("Transaction ID: {}, 201 CREATED, Trainer {} was created", transactionId,  authData.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(authData);
    }

    @ApiOperation(value = "Get trainer", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainer returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainers/{} was called", transactionId, username);
        try{
            userService.validateUsername(username);
            Trainer trainer = trainerService.getByUsername(transactionId, username);
            logger.info("Transaction ID: {}, 200 OK, Trainer was returned {}", transactionId, trainer);
            return ResponseEntity.status(HttpStatus.OK).body(trainer);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Update trainer", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainer updated"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PutMapping("/{username}")
    public ResponseEntity<?> updateTrainer(@RequestBody TrainerData trainerData, @PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, PUT /trainers/{} was called with body {}", transactionId, username,trainerData);
        try{
            userService.validateUsername(username);
            Optional.ofNullable(trainerData.isActive())
                    .orElseThrow(()-> new IllegalArgumentException("isActive is required"));
            trainerData.validate();
            Trainer trainer = trainerService.update(transactionId, trainerData,username);
            logger.info("Transaction ID: {}, 200 OK, Trainer was updated {}", transactionId, trainer);
            return ResponseEntity.status(HttpStatus.OK).body(trainer);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Get active trainers that not assigned to trainee", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, List of trainers returned"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @GetMapping("/not-assigned-on-trainee/{username}")
    public ResponseEntity<?> getNotAssignedOnTrainee(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, GET /trainers/not-assigned-on-trainee/{} was called with body ", transactionId, username);
        try{
            userService.validateUsername(username);
            List<Trainer> trainers = trainerService.getNotAssignedOnTrainee(transactionId, username);
            logger.info("Transaction ID: {}, 200 OK, List of active trainers without trainees was returned {}", transactionId, trainers);
            return ResponseEntity.status(HttpStatus.OK).body(trainers);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Switch trainer's active status", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK, Trainer's active status switched"),
            @ApiResponse(code = 400, message = "BAD_REQUEST, the request body or params are invalid or incorrect"),
    })
    @PatchMapping("/{username}/switch-active-status")
    public ResponseEntity<?> changeActiveStatus(@PathVariable("username") String username){
        UUID transactionId = UUID.randomUUID();
        logger.info("Transaction ID: {}, PATCH /trainers/{}/switch-active-status was called",transactionId, username);
        try{
            userService.validateUsername(username);
            boolean status = trainerService.changeActiveStatus(transactionId, username);
            logger.info("Transaction ID: {}, 200 OK, Trainer's active status was switched to {}", transactionId, status);
            return ResponseEntity.status(HttpStatus.OK).body("Active status switched to "+status);
        }catch (IllegalArgumentException e){
            logger.info("Transaction ID: {}, 400 BAD_REQUEST, {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
