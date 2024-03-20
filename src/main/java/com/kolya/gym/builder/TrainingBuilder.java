package com.kolya.gym.builder;

import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingBuilder {
    private Training training;

    public TrainingBuilder setTrainee(Trainee trainee){
        training.setTrainee(trainee);
        return this;
    }

    public TrainingBuilder setTrainer(Trainer trainer){
        training.setTrainer(trainer);
        return this;
    }

    public TrainingBuilder setTrainingDate(Date date){
        training.setTrainingDate(date);
        return this;
    }

    public TrainingBuilder setTrainingType(TrainingType type){
        training.setTrainingType(type);
        return this;
    }

    public TrainingBuilder setDuration(int dur){
        training.setDuration(dur);
        return this;
    }

    public TrainingBuilder setTrainingName(String name){
        training.setTrainingName(name);
        return this;
    }

    public Training build(){
        return training;
    }
}
