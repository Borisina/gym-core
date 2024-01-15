package com.kolya.gym.builder;

import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingDataBuilder {
    private final TrainingData trainingData;

    public TrainingDataBuilder() {
        trainingData = new TrainingData();
    }

    public TrainingDataBuilder setTraineeUsername(String username){
        trainingData.setTraineeUsername(username);
        return this;
    }

    public TrainingDataBuilder setTrainerUsername(String username){
        trainingData.setTrainerUsername(username);
        return this;
    }

    public TrainingDataBuilder setTrainingName(String trainingName){
        trainingData.setTrainingName(trainingName);
        return this;
    }

    public TrainingDataBuilder setTrainingType(TrainingType type){
        trainingData.setTrainingType(type);
        return this;
    }

    public TrainingDataBuilder setTrainingDate(Date date){
        trainingData.setTrainingDate(date);
        return this;
    }

    public TrainingDataBuilder setDuration(int duration){
        trainingData.setDuration(duration);
        return this;
    }

    public TrainingData build(){
        return trainingData;
    }
}
