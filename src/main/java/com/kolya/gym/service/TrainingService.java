package com.kolya.gym.service;

import com.kolya.gym.builder.TrainerWorkloadRequestDataBuilder;
import com.kolya.gym.builder.TrainingDataBuilder;
import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import com.kolya.gym.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TrainingService {

    private final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    private TrainingRepo trainingRepo;
    @Autowired
    private TrainerRepo trainerRepo;
    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private TrainerWorkloadService trainerWorkloadService;

    public Training create(UUID transactionId, TrainingData trainingData){
        logger.info("Transaction ID: {}, Creating training with data: {}", transactionId, trainingData);
        Training training = getTrainingFromData(transactionId, trainingData);
        TrainerWorkloadRequestData requestData = trainerWorkloadService.getRequestDataFromTraining(training, ActionType.ADD);
        trainerWorkloadService.changeWorkload(transactionId, requestData);
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
        List<TrainingData> trainingDataSet = trainingsToTrainingDataList(trainingList);
        logger.info("Transaction ID: {}, Trainings were returned {}", transactionId, trainingDataSet);
        return  trainingDataSet;
    }

    public List<TrainingData> getByTrainerUsernameAndCriteria(UUID transactionId, String username, TrainingCriteria trainingCriteria) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting trainings by trainer = {} and criteria = {}", transactionId, username, trainingCriteria);
        Trainer trainer = trainerRepo.findByUserUsername(username)
                    .orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+username));
        String trainingType=null;
        if (trainingCriteria.getTrainingType()!=null){
            trainingType = trainingCriteria.getTrainingType().name();
        }
        List<Training> trainingSet = trainingRepo.findByTrainerIdAndCriteria(
                trainer.getId(),
                trainingType,
                trainingCriteria.getTrainingName(),
                trainingCriteria.getTrainingDateFrom(),
                trainingCriteria.getTrainingDateTo());

        List<TrainingData> trainingDataList = trainingsToTrainingDataList(trainingSet);
        logger.info("Transaction ID: {}, Trainings were returned {}", transactionId, trainingDataList);
        return  trainingDataList;
    }

    @Transactional(readOnly = true)
    private Training getTrainingFromData(UUID transactionId, TrainingData data) throws IllegalArgumentException{
        logger.info("Transaction ID: {}, Getting Training From TrainingData: {}", transactionId, data);
        Training training = new Training();
        Trainer trainer = trainerRepo.findByUserUsername(data.getTrainerUsername()).orElseThrow(()->new IllegalArgumentException("There is no trainer with username = "+data.getTrainerUsername()));
        Trainee trainee = traineeRepo.findByUserUsername(data.getTraineeUsername()).orElseThrow(()->new IllegalArgumentException("There is no trainee with username = "+data.getTraineeUsername()));
        trainee.getTrainersSet().add(trainer);
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

    public List<TrainingData> trainingsToTrainingDataList(Collection<Training> trainings){
        List<TrainingData> trainingDataList = new ArrayList<>();
        trainings.forEach(training -> trainingDataList.add(getTrainingData(training)));
        return trainingDataList;
    }

    public boolean isEmptyCriteria(TrainingCriteria trainingCriteria){
        return trainingCriteria.getTrainingDateTo()==null
                && trainingCriteria.getTrainingDateFrom()==null
                && trainingCriteria.getTrainingName()==null
                && trainingCriteria.getTrainingType()==null;
    }

    public List<TrainingType> getTrainingTypes(UUID transactionId) {
        logger.info("Transaction ID: {}, Getting ", transactionId);
        List<TrainingType> types = Arrays.asList(TrainingType.values());
        logger.info("Transaction ID: {}, Training Types were returned {}", transactionId,types);
        return types;
    }


}
