package com.kolya.gym.service;

import com.kolya.gym.data.TrainingCriteria;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.*;
import com.kolya.gym.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingService {
    private final TrainingRepo trainingRepo;
    private final TrainerRepo trainerRepo;
    private final TraineeRepo traineeRepo;
    private UserRepo userRepo;

    @Autowired
    public TrainingService(TrainingRepo trainingRepo, TrainerRepo trainerRepo, TraineeRepo traineeRepo, UserRepo userRepo) {
        this.trainingRepo = trainingRepo;
        this.trainerRepo = trainerRepo;
        this.traineeRepo = traineeRepo;
        this.userRepo = userRepo;
    }

    public Training create(TrainingData trainingData){
        Training training = getTrainingFromData(trainingData);
        return trainingRepo.save(training);
    }

    public Training get(long id) throws IllegalArgumentException{
        return trainingRepo.findById(id).orElseThrow(()->new IllegalArgumentException("There is no training with id = "+id));
    }

    private Training getTrainingFromData(TrainingData data) throws IllegalArgumentException{
        Training training = new Training();
        Trainer trainer = trainerRepo.findById(data.getTrainerId()).orElseThrow(()->new IllegalArgumentException("There is no trainer with id = "+data.getTrainerId()));
        Trainee trainee = traineeRepo.findById(data.getTraineeId()).orElseThrow(()->new IllegalArgumentException("There is no trainee with id = "+data.getTrainerId()));
        training.setTrainingDate(data.getTrainingDate());
        training.setTrainingType(data.getTrainingType());
        training.setTrainingName(data.getTrainingName());
        training.setDuration(data.getDuration());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        return training;
    }

    @Transactional
    public List<Training> getByTraineeUsernameAndCriteria(String username, TrainingCriteria trainingCriteria) throws IllegalArgumentException{
        User user = userRepo.findByUsername(username).orElseThrow(()->new IllegalArgumentException("There is no user with username = "+username));
        Trainee trainee = traineeRepo.findByUserId(user.getId()).orElseThrow(()->new IllegalArgumentException("There is no trainee with id = "+user.getId()));
        if (trainingCriteria==null){
            return trainingRepo.findByTrainee(trainee);
        }else{
            return trainingRepo.findByTraineeIdAndCriteria(
                    trainee.getId(),
                    trainingCriteria.getTrainingType().name(),
                    trainingCriteria.getTrainingName(),
                    trainingCriteria.getTrainingDateFrom(),
                    trainingCriteria.getTrainingDateTo(),
                    trainingCriteria.getDurationMin(),
                    trainingCriteria.getDurationMax());
        }

    }

    @Transactional
    public List<Training> getByTrainerUsernameAndCriteria(String username, TrainingCriteria trainingCriteria) throws IllegalArgumentException{
        User user = userRepo.findByUsername(username).orElseThrow(()->new IllegalArgumentException("There is no user with username = "+username));
        Trainer trainer = trainerRepo.findByUserId(user.getId()).orElseThrow(()->new IllegalArgumentException("There is no trainer with id = "+user.getId()));
        if (trainingCriteria==null){
            return trainingRepo.findByTrainer(trainer);
        }else{
            return trainingRepo.findByTrainerIdAndCriteria(
                    trainer.getId(),
                    trainingCriteria.getTrainingType().name(),
                    trainingCriteria.getTrainingName(),
                    trainingCriteria.getTrainingDateFrom(),
                    trainingCriteria.getTrainingDateTo(),
                    trainingCriteria.getDurationMin(),
                    trainingCriteria.getDurationMax());
        }

    }

    public List<Training> getAll() {
        return trainingRepo.findAll();
    }
}
