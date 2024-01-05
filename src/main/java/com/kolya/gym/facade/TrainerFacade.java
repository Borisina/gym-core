package com.kolya.gym.facade;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.service.TrainerService;
import com.kolya.gym.service.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.kolya.gym.validation.CommonValidation.nullValidation;

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
            nullValidation(trainerData);
            trainerData.validate();
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
        Trainer trainer = trainerService.create(trainerData);
        String username = trainer.getUser().getUsername();
        String password = trainer.getUser().getPassword();
        System.out.println("Trainer created: Username = "+username+"; Password = "+password+";");
        logger.info("Trainer created." + trainer);
        return trainer;
    }

    public Trainer updateTrainer(AuthData authData, TrainerData trainerData, long trainerId){
        try{
            userService.authenticate(authData);
            nullValidation(trainerData);
            trainerData.validateCharacters();
            Trainer trainer = trainerService.update(trainerData,trainerId);
            System.out.println("Trainer updated.");
            logger.info("Trainer updated. " + trainer);
            return trainer;
        }catch (AuthenticationException | IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
    }

    public Trainer getTrainer(long id){
        try{
            Trainer trainer = trainerService.get(id);
            logger.info("Get trainer. " + trainer);
            return trainer;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
    }

    public Trainer getByUsername(String username){
        try{
            Trainer trainer = trainerService.getByUsername(username);
            logger.info("Get trainer by username. " + trainer);
            return trainer;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Trainer> getAllTrainers(){
        logger.info("Get allTrainers.");
        return trainerService.getAll();
    }

    public void changeActiveStatus(long id){
        try{
            userService.changeActiveStatus(id);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
    }


}
