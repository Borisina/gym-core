package com.kolya.gym.service;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TrainerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TrainerService {

    private final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerRepo trainerRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public AuthData create(UUID transactionId, TrainerData trainerData){
        logger.info("Transaction ID: {}, Creating trainer with data: {}", transactionId, trainerData);
        Trainer trainer = getTrainerFromDataForCreate(transactionId, trainerData);
        String password = trainer.getUser().getPassword();
        trainer.getUser().setPassword(userService.encodePassword(password));
        trainerRepo.save(trainer);
        logger.info("Transaction ID: {}, Trainer was created: {}", transactionId, trainer);
        return new AuthData(trainer.getUser().getUsername(),password);
    }

    @Transactional
    public Trainer update(UUID transactionId, TrainerData trainerData, String username) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Updating trainer with username {} and data: {}", transactionId, username, trainerData);
        Trainer updatedTrainer = getTrainerFromDataForUpdate(transactionId, trainerData);
        Trainer trainer = trainerRepo.findByUserUsername(username).orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+username));
        trainer.setSpecialization(updatedTrainer.getSpecialization());
        userService.change(trainer.getUser(),updatedTrainer.getUser());
        logger.info("Transaction ID: {}, Trainer was updated: {}", transactionId, trainer);
        return trainer;
    }


    public Trainer getByUsername(UUID transactionId, String username) throws IllegalArgumentException {
        logger.info("Transaction ID: {}, Getting trainer with username {}", transactionId, username);
        Trainer trainer =  trainerRepo.findByUserUsername(username).orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+username));
        logger.info("Transaction ID: {}, Trainer with username {} was returned", transactionId, username);
        return trainer;
    }

    private  Trainer getTrainerFromData(TrainerData trainerData){
        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainerData.getSpecialization());
        return trainer;
    }

    private Trainer getTrainerFromDataForCreate(UUID transactionId, TrainerData trainerData){
        Trainer trainer = getTrainerFromData(trainerData);
        System.out.println("getTrainerFromDataForCreate " +trainer);
        User user = userService.generateUserForCreate(transactionId, trainerData);
        trainer.setUser(user);
        return trainer;
    }

    private Trainer getTrainerFromDataForUpdate(UUID transactionId, TrainerData trainerData){
        Trainer trainer = getTrainerFromData(trainerData);
        User user = userService.generateUserForUpdate(transactionId, trainerData);
        trainer.setUser(user);
        return trainer;
    }

    public List<Trainer> getNotAssignedOnTrainee(UUID transactionId, String username) {
        return trainerRepo.findNotAssignedOnTrainee(username);
    }

    @Transactional
    public boolean changeActiveStatus(UUID transactionId, String username){
        logger.info("Transaction ID: {}, Switching trainer's ({}) active status", transactionId, username);
        Trainer trainer = trainerRepo.findByUserUsername(username).orElseThrow(()-> new IllegalArgumentException("There is no trainer with username = "+username));
        boolean newStatus = !trainer.getUser().isActive();
        trainer.getUser().setActive(newStatus);
        logger.info("Transaction ID: {}, Trainer's ({}) active status was switched to {}", transactionId, username,  newStatus);
        return newStatus;
    }

    public List<Training> getTrainings(UUID transactionId, String username) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting trainer's ({}) trainings", transactionId, username);
        Trainer trainer = getByUsername(transactionId, username);
        logger.info("Transaction ID: {}, Trainer's ({}) trainings were returned: {}", transactionId, username, trainer.getTrainingsList());
        return trainer.getTrainingsList();
    }
}
