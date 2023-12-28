package com.kolya.gym.facade;

import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.*;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerFacade {

    private final TrainerService trainerService;

    private final UserService userService;

    private final Logger logger;

    @Autowired
    public TrainerFacade(TrainerService trainerService, UserService userService, Logger logger) {
        this.trainerService = trainerService;
        this.userService = userService;
        this.logger = logger;
    }

    public Trainer createTrainer(TrainerData trainerData){
        try{
            trainerData.isValid();
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
        User user = userService.create(trainerData.getFirstName(),trainerData.getLastName());
        logger.info("User created. " + user);
        Trainer trainer = trainerService.create(trainerData,user);
        logger.info("Trainer created. " + trainer);
        return trainer;
    }

    public Trainer updateTrainer(TrainerData trainerData, long trainerId){
        try{
            trainerData.isValidCharacters();
            Trainer trainer = trainerService.get(trainerId);
            User user = userService.update(trainerData.getFirstName(),trainerData.getLastName(),trainer.getUser().getId());
            trainer = trainerService.update(trainerData, user, trainerId);
            logger.info("Trainer updated. " + trainer);
            return trainer;
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }

    }

    public Trainer getTrainer(long id){
        try{
            Trainer trainer = trainerService.get(id);
            logger.info("Get trainer. " + trainer);
            return trainer;
        }catch(IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Trainer> getAllTrainers(){
        logger.info("Get all trainers.");
        return trainerService.getAll();
    }
}
