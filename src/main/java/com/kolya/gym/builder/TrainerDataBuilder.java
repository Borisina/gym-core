package com.kolya.gym.builder;

import com.kolya.gym.data.TrainerData;

public class TrainerDataBuilder {
    private TrainerData trainerData;

    public TrainerDataBuilder setFirstName(String firstName){
        trainerData.setFirstName(firstName);
        return this;
    }

    public TrainerDataBuilder setLastName(String lastName){
        trainerData.setLastName(lastName);
        return this;
    }

    public TrainerDataBuilder setSpecialization(String specialization){
        trainerData.setLastName(specialization);
        return this;
    }

    public TrainerData build(){
        return trainerData;
    }
}
