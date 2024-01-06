package com.kolya.gym.facade;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import com.kolya.gym.service.TrainingService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class TrainingFacade {
    private final TrainingService trainingService;

    private final Logger logger;

    @Autowired
    public TrainingFacade(TrainingService trainingService, Logger logger) {
        this.trainingService = trainingService;
        this.logger = logger;
    }

    public Training createTraining(TrainingData trainingData){
        try{
            Optional.ofNullable(trainingData).orElseThrow(()->new IllegalArgumentException("TrainingData is null"));
            trainingData.validate();
            Training training = trainingService.create(trainingData);
            logger.info("Training created. " + training);
            return training;
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public Training getTraining(long id){
        try{
            Training training = trainingService.get(id);
            logger.info("Get training. " + training);
            return training;
        }catch(IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Training> getAllTrainings(){
        logger.info("Get all trainings." );
        return trainingService.getAll();
    }

    public List<Training> getByTraineeUsernameAndCriteria(String username, TrainingCriteria trainingCriteria){
        try{
            Optional.ofNullable(username).orElseThrow(()->new IllegalArgumentException("Username is null"));
            if (trainingCriteria!=null){
                trainingCriteria.validate();
            }
            logger.info("Get trainings by trainee's username and criteria.");
            return trainingService.getByTraineeUsernameAndCriteria(username,trainingCriteria);
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }

    public List<Training> getByTrainerUsernameAndCriteria(String username, TrainingCriteria trainingCriteria){
        try{
            Optional.ofNullable(username).orElseThrow(()->new IllegalArgumentException("Username is null"));
            if (trainingCriteria!=null){
                trainingCriteria.validate();
            }
            logger.info("Get trainings by trainer's username and criteria.");
            return trainingService.getByTrainerUsernameAndCriteria(username,trainingCriteria);
        }catch (IllegalArgumentException e){
            logger.info(e.getMessage());
            return null;
        }
    }
}
