package com.kolya.gym.service;

import com.kolya.gym.builder.TrainingDataBuilder;
import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import com.kolya.gym.repo.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingService {
    private final TrainingRepo trainingRepo;
    private final TrainerRepo trainerRepo;
    private final TraineeRepo traineeRepo;

    private final Logger logger;

    @Autowired
    public TrainingService(TrainingRepo trainingRepo, TrainerRepo trainerRepo, TraineeRepo traineeRepo, Logger logger) {
        this.trainingRepo = trainingRepo;
        this.trainerRepo = trainerRepo;
        this.traineeRepo = traineeRepo;
        this.logger = logger;
    }

    public Training create(UUID transactionId, TrainingData trainingData){
        logger.info("Transaction ID: {}, Creating training with data: {}", transactionId, trainingData);
        Training training = getTrainingFromData(transactionId, trainingData);
        training = trainingRepo.save(training);
        logger.info("Transaction ID: {}, Training was created: {}", transactionId, training);
        return training;
    }

    @Transactional
    public List<TrainingData> getByTraineeUsernameAndCriteria(UUID transactionId, String username, TrainingCriteria trainingCriteria) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting trainings by trainee = {} and criteria = {}", transactionId, username, trainingCriteria);
        Trainee trainee = traineeRepo.findByUserUsername(username)
                .orElseThrow(()->new IllegalArgumentException("There is no trainee with username = "+username));
        String trainingType=null;
        if (trainingCriteria.getTrainingType()!=null){
            trainingType = trainingCriteria.getTrainingType().name();
        }
        List<Training> trainingList = trainingRepo.findByTraineeIdAndCriteria(
                trainee.getId(),
                trainingType,
                trainingCriteria.getTrainingName(),
                trainingCriteria.getTrainingDateFrom(),
                trainingCriteria.getTrainingDateTo());
        List<TrainingData> trainingDataList = trainingListToTrainingDataList(trainingList);
        logger.info("Transaction ID: {}, Trainings were returned {}", transactionId, trainingDataList);
        return  trainingDataList;
    }

    public List<TrainingData> getByTrainerUsernameAndCriteria(UUID transactionId, String username, TrainingCriteria trainingCriteria) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting trainings by trainer = {} and criteria = {}", transactionId, username, trainingCriteria);
        Trainer trainer = trainerRepo.findByUserUsername(username)
                    .orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+username));
        String trainingType=null;
        if (trainingCriteria.getTrainingType()!=null){
            trainingType = trainingCriteria.getTrainingType().name();
        }
        List<Training> trainingList = trainingRepo.findByTrainerIdAndCriteria(
                trainer.getId(),
                trainingType,
                trainingCriteria.getTrainingName(),
                trainingCriteria.getTrainingDateFrom(),
                trainingCriteria.getTrainingDateTo());

        List<TrainingData> trainingDataList = trainingListToTrainingDataList(trainingList);
        logger.info("Transaction ID: {}, Trainings were returned {}", transactionId, trainingDataList);
        return  trainingDataList;
    }

    @Transactional(readOnly = true)
    private Training getTrainingFromData(UUID transactionId, TrainingData data) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting Training From TrainingData: {}", transactionId, data);
        Training training = new Training();
        Trainer trainer = trainerRepo.findByUserUsername(data.getTrainerUsername()).orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+data.getTrainerUsername()));
        Trainee trainee = traineeRepo.findByUserUsername(data.getTraineeUsername()).orElseThrow(()->new IllegalArgumentException("There is no trainee with username = "+data.getTraineeUsername()));
        trainer.getTraineesList().add(trainee);
        trainee.getTrainersList().add(trainer);
        training.setTrainingDate(data.getTrainingDate());
        training.setTrainingType(data.getTrainingType());
        training.setTrainingName(data.getTrainingName());
        training.setDuration(data.getDuration());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        logger.info("Transaction ID: {}, Training From TrainingData was returned: {}", transactionId, training);
        return training;
    }

    public TrainingData getTrainingData(Training training){
        return new TrainingDataBuilder()
                .setTrainingDate(training.getTrainingDate())
                .setTrainingName(training.getTrainingName())
                .setTrainingType(training.getTrainingType())
                .setDuration(training.getDuration())
                .setTrainerUsername(training.getTrainer().getUser().getUsername())
                .setTraineeUsername(training.getTrainee().getUser().getUsername())
                .build();
    }

    public List<TrainingData> trainingListToTrainingDataList(List<Training> trainingList){
        List<TrainingData> trainingDataList = new ArrayList<>();
        for (Training training:trainingList){
            trainingDataList.add(getTrainingData(training));
        }
        return trainingDataList;
    }

    public boolean isEmptyCriteria(TrainingCriteria trainingCriteria){
        return trainingCriteria.getTrainingDateTo()==null
                && trainingCriteria.getTrainingDateFrom()==null
                && trainingCriteria.getTrainingName()==null
                && trainingCriteria.getTrainingType()==null;
    }
}
