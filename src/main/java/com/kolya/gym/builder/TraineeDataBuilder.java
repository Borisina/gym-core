package com.kolya.gym.builder;

import com.kolya.gym.data.TraineeData;

import java.util.Date;

public class TraineeDataBuilder {
    private final TraineeData traineeData;

    public TraineeDataBuilder() {
        traineeData = new TraineeData();
    }

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
