package com.kolya.gym.facade;

import com.kolya.gym.data.AuthData;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TraineeDataUpdate;
import com.kolya.gym.data.UserData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.User;
import com.kolya.gym.service.TraineeService;
import com.kolya.gym.service.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.kolya.gym.validation.CommonValidation.nullValidation;

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
            nullValidation(traineeData);
            traineeData.validate();
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
        Trainee trainee = traineeService.create(traineeData);
        String username = trainee.getUser().getUsername();
        String password = trainee.getUser().getPassword();
        System.out.println("Trainee created: Username = "+username+"; Password = "+password+";");
        logger.info("Trainee created. " + trainee);
        return trainee;
    }

    public Trainee updateTrainee(AuthData authData, TraineeDataUpdate traineeDataUpdate){
        try{
            nullValidation(authData);
            userService.authenticate(authData);
            nullValidation(traineeDataUpdate);
            traineeDataUpdate.validate();
            Trainee trainee = traineeService.update(traineeDataUpdate);
            System.out.println("Trainee updated.");
            logger.info("Trainee updated. " + trainee);
            return trainee;
        }catch (AuthenticationException | IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Trainee> updateTraineeList(AuthData authData, List<TraineeDataUpdate> traineeDataUpdateList){
        try{
            nullValidation(authData);
            userService.authenticate(authData);
            nullValidation(traineeDataUpdateList);
            List<Trainee> trainees = traineeService.updateList(traineeDataUpdateList);
            System.out.println("Trainees updated.");
            logger.info("Trainees updated.");
            return trainees;
        }catch (AuthenticationException | IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public Trainee deleteByUsername(AuthData authData, String username){
        try{
            nullValidation(authData);
            userService.authenticate(authData);
            Trainee trainee = traineeService.deleteByUsername(username);
            System.out.println("Trainee deleted.");
            logger.info("Trainee deleted. " + trainee);
            return trainee;
        }catch (AuthenticationException | IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
    }

    public Trainee getTrainee(long id){
        try{
            Trainee trainee = traineeService.get(id);
            logger.info("Get trainee by id. " + trainee);
            return trainee;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
    }

    public Trainee getByUsername(String username){
        try{
            Trainee trainee = traineeService.getByUsername(username);
            logger.info("Get trainee by username. " + trainee);
            return trainee;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Trainee> getAllTrainees(){
        logger.info("Get allTrainees.");
        return traineeService.getAll();
    }

    public void changePassword(AuthData authData, String newPassword){
        try{
            nullValidation(authData);
            User authedUser = userService.authenticate(authData);
            userService.changePassword(authedUser,newPassword);
            logger.info("Password changed for user: " + authedUser);
            System.out.println("Password changed.");
        }catch (AuthenticationException | IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
    }

    public void changeActiveStatus(long id){
        try{
           userService.changeActiveStatus(id);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
    }

    public List<Trainee> getNotAssignedTrainees(){
        return traineeService.getNotAssigned();
    }
}
