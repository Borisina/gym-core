package com.kolya.gym.builder;

import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;

import java.util.Date;

public class TrainerWorkloadRequestDataBuilder {
    private TrainerWorkloadRequestData trainerWorkloadRequestData;

    public TrainerWorkloadRequestDataBuilder() {
        this.trainerWorkloadRequestData = new TrainerWorkloadRequestData();
    }

    public TrainerWorkloadRequestDataBuilder setUsername(String username){
        trainerWorkloadRequestData.setUsername(username);
        return this;
    }

    public TrainerWorkloadRequestDataBuilder setFirstName(String firstName){
        trainerWorkloadRequestData.setFirstName(firstName);
        return this;
    }

    public TrainerWorkloadRequestDataBuilder setLastName(String lastName){
        trainerWorkloadRequestData.setLastName(lastName);
        return this;
    }

    public TrainerWorkloadRequestDataBuilder setActive(boolean isActive){
        trainerWorkloadRequestData.setActive(isActive);
        return this;
    }

    public TrainerWorkloadRequestDataBuilder setTrainingDate(Date TrainingDate){
        trainerWorkloadRequestData.setTrainingDate(TrainingDate);
        return this;
    }

    public TrainerWorkloadRequestDataBuilder setTrainingDuration(int duration){
        trainerWorkloadRequestData.setTrainingDuration(duration);
        return this;
    }

    public TrainerWorkloadRequestDataBuilder setActionType(ActionType actionType){
        trainerWorkloadRequestData.setActionType(actionType);
        return this;
    }

    public TrainerWorkloadRequestData build(){
        return trainerWorkloadRequestData;
    }
}
