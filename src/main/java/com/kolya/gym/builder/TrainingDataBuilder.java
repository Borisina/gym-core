package com.kolya.gym.builder;

import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingDataBuilder {
    private TrainingData trainingData;

    public TrainingDataBuilder setTraineeId(long id){
        trainingData.setTraineeId(id);
        return this;
    }

    public TrainingDataBuilder setTrainerId(long id){
        trainingData.setTrainerId(id);
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
