package com.kolya.gym.builder;

import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.TrainingType;

public class TrainerDataBuilder {
    private final TrainerData trainerData;

    public TrainerDataBuilder() {
        trainerData = new TrainerData();
    }

    public TrainerDataBuilder setFirstName(String firstName){
        trainerData.setFirstName(firstName);
        return this;
    }

    public TrainerDataBuilder setLastName(String lastName){
        trainerData.setLastName(lastName);
        return this;
    }

    public TrainerDataBuilder setSpecialization(TrainingType specialization){
        trainerData.setSpecialization(specialization);
        return this;
    }

    public TrainerData build(){
        return trainerData;
    }
}
