package com.kolya.gym.builder;

import com.kolya.gym.data.TraineeData;
import com.kolya.gym.data.TrainerData;

import java.util.Date;

public class TraineeDataBuilder {
    private TraineeData traineeData;

    public TraineeDataBuilder setFirstName(String firstName){
        traineeData.setFirstName(firstName);
        return this;
    }

    public TraineeDataBuilder setLastName(String lastName){
        traineeData.setLastName(lastName);
        return this;
    }

    public TraineeDataBuilder setDateOfBirth(Date date){
        traineeData.setDateOfBirth(date);
        return this;
    }

    public TraineeDataBuilder setAddress(String address){
        traineeData.setAddress(address);
        return this;
    }

    public TraineeData build(){
        return traineeData;
    }
}
