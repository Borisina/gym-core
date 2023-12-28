package com.kolya.gym.facade;

import com.kolya.gym.domain.Trainee;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.User;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeFacade {
    private final TraineeService traineeService;
    private final UserService userService;

    private final Logger logger;

    @Autowired
    public TraineeFacade(TraineeService traineeService, UserService userService, Logger logger) {
        this.traineeService = traineeService;
        this.userService = userService;
        this.logger = logger;
    }

    public Trainee createTrainee(TraineeData traineeData){
        try{
            traineeData.isValid();
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
        User user = userService.create(traineeData.getFirstName(),traineeData.getLastName());
        logger.info("User created. " + user);
        Trainee trainee = traineeService.create(traineeData,user);
        logger.info("Trainee created. " + trainee);
        return trainee;
    }

    public Trainee updateTrainee(TraineeData traineeData, long traineeId){
        try{
            traineeData.isValidCharacters();
            Trainee trainee = traineeService.get(traineeId);
            User user = userService.update(traineeData.getFirstName(),traineeData.getLastName(),trainee.getUser().getId());
            trainee = traineeService.update(traineeData, user, traineeId);
            logger.info("Trainee updated. " + trainee);
            return trainee;
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public Trainee deleteTrainee(long id){
        Trainee trainee;
        try{
            trainee = traineeService.delete(id);
            logger.info("Trainee deleted. " + trainee);
            User user = userService.delete(trainee.getId());
            logger.info("User deleted. " + user);
        }catch(IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
        return trainee;
    }

    public Trainee getTrainee(long id){
        try{
            Trainee trainee = traineeService.get(id);
            logger.info("Get trainee. " + trainee);
            return trainee;
        }catch(IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Trainee> getAllTrainees(){
        logger.info("Get allTrainees.");
        return traineeService.getAll();
    }


}
