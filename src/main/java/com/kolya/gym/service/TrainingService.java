package com.kolya.gym.service;

import com.kolya.gym.dao.TraineeDao;
import com.kolya.gym.dao.TrainerDao;
import com.kolya.gym.dao.TrainingDao;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    private final TrainingDao trainingDao;
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;

    @Autowired
    public TrainingService(TrainingDao trainingDao, TrainerDao trainerDao, TraineeDao traineeDao) {
        this.trainingDao = trainingDao;
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
    }

    public Training create(TrainingData trainingData) throws IllegalArgumentException{
        Training training = getTrainingFromData(trainingData);
        return trainingDao.create(training);
    }

    public Training get(long id) throws IllegalArgumentException{
        Training training = trainingDao.get(id);
        if (training==null){
            throw new IllegalArgumentException("There is no training with id = "+id);
        }
        return training;
    }

    private Training getTrainingFromData(TrainingData data) throws IllegalArgumentException{
        Training training = new Training();
        Trainer trainer = trainerDao.get(data.getTrainerId());
        if (trainer==null){
            throw new IllegalArgumentException("There is no trainer with id = "+data.getTrainerId());
        }
        Trainee trainee = traineeDao.get(data.getTraineeId());
        if (trainee==null){
            throw new IllegalArgumentException("There is no trainee with id = "+data.getTraineeId());
        }
        training.setTrainingDate(data.getTrainingDate());
        training.setTrainingType(TrainingType.valueOf(data.getTrainingType().toUpperCase()));
        training.setTrainingName(data.getTrainingName());
        training.setDuration(data.getDuration());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        return training;
    }

    public List<Training> getAll() {
        return trainingDao.getAll();
    }
}
