package com.kolya.gym.service;

import com.kolya.gym.controller.TrainingController;
import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TraineeService {
    private final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeRepo traineeRepo;
    @Autowired
    private TrainerRepo trainerRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private TrainerWorkloadService trainerWorkloadService;


    @Transactional
    public AuthData create(UUID transactionId, TraineeData traineeData){
        logger.info("Transaction ID: {}, Creating trainee with data: {}", transactionId, traineeData);
        Trainee trainee = getTraineeFromDataForCreate(transactionId, traineeData);
        String password = trainee.getUser().getPassword();
        trainee.getUser().setPassword(userService.encodePassword(password));
        traineeRepo.save(trainee);
        logger.info("Transaction ID: {}, Trainee was created: {}", transactionId, trainee);
        return new AuthData(trainee.getUser().getUsername(),password);
    }

    @Transactional
    public Trainee update(UUID transactionId, TraineeDataUpdate traineeDataUpdate) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Updating trainee with data: {}", transactionId, traineeDataUpdate);
        Trainee updatedTrainee = getTraineeFromDataForUpdate(transactionId, traineeDataUpdate);
        Trainee trainee = getByUsername(transactionId, traineeDataUpdate.getUsername());
        trainee.setAddress(updatedTrainee.getAddress());
        trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
        userService.change(trainee.getUser(),updatedTrainee.getUser());
        logger.info("Transaction ID: {}, Trainee was updated: {}", transactionId, trainee);
        return trainee;
    }

    @Transactional
    public Set<Trainer> updateList(UUID transactionId, List<String> trainersUsernamesList, String username) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Updating trainee's ({}) trainersList with data: {}", transactionId, username, trainersUsernamesList);
        Trainee trainee = getByUsername(transactionId, username);
        Set<Trainer> trainerSet = new HashSet<>();
        trainersUsernamesList.forEach( trainersUsername -> {
                    Trainer trainerFromDb = trainerRepo.findByUserUsername(trainersUsername)
                            .orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+username));
                    trainerSet.add(trainerFromDb);
                });
        trainee.setTrainersSet(trainerSet);
        logger.info("Transaction ID: {}, Trainee's ({}) trainersList was updated: {}",transactionId, username, trainersUsernamesList);
        return trainerSet;
    }

    public Trainee getByUsername(UUID transactionId, String username) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting trainee with username {}", transactionId, username);
        Trainee trainee = traineeRepo.findByUserUsername(username).orElseThrow(()->new IllegalArgumentException("There is no trainee with username = "+username));
        logger.info("Transaction ID: {}, Trainee with username {} was returned", transactionId, username);
        return trainee;
    }

    @Transactional
    public Trainee deleteByUsername(UUID transactionId, String username) throws IllegalArgumentException {
        logger.info("Transaction ID: {}, Deleting trainee with username {}", transactionId, username);
        Trainee trainee = getByUsername(transactionId, username);
        Set<Training> trainings = trainee.getTrainingsSet();
        traineeRepo.deleteByUserUsername(username);
        trainerWorkloadService.deleteTrainings(transactionId, trainings);
        logger.info("Transaction ID: {}, Trainee with username {} was deleted", transactionId, username);
        return trainee;
    }

    @Transactional
    public boolean changeActiveStatus(UUID transactionId, String username) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Switching trainee's ({}) active status", transactionId, username);
        Trainee trainee = getByUsername(transactionId, username);
        boolean newStatus = !trainee.getUser().isActive();
        trainee.getUser().setActive(newStatus);
        logger.info("Transaction ID: {}, Trainee's ({}) active status was switched to {}", transactionId, username,  newStatus);
        return newStatus;
    }

    public Set<Training> getTrainings(UUID transactionId, String username) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting trainee's ({}) trainings", transactionId, username);
        Trainee trainee = getByUsername(transactionId, username);
        logger.info("Transaction ID: {}, Trainee's ({}) trainings were returned {}", transactionId, username, trainee.getTrainingsSet());
        return trainee.getTrainingsSet();
    }

    public Trainee getTraineeFromData(TraineeData traineeData){
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeData.getAddress());
        trainee.setDateOfBirth(traineeData.getDateOfBirth());
        return trainee;
    }


    private Trainee getTraineeFromDataForCreate(UUID transactionId, TraineeData traineeData){
        Trainee trainee = getTraineeFromData(traineeData);
        User user = userService.generateUserForCreate(transactionId, traineeData);
        trainee.setUser(user);
        return trainee;
    }

    private Trainee getTraineeFromDataForUpdate(UUID transactionId, TraineeData traineeData){
        Trainee trainee = getTraineeFromData(traineeData);
        User user = userService.generateUserForUpdate(transactionId, traineeData);
        trainee.setUser(user);
        return trainee;
    }
}
