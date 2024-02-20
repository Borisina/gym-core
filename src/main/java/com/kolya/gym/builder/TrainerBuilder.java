package com.kolya.gym.builder;

import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.TrainingType;
import com.kolya.gym.domain.User;

public class TrainerBuilder {
    private Trainer trainer;

    public TrainerBuilder(){
        trainer = new Trainer();
        trainer.setUser(new User());
    }

    public TrainerBuilder setUsername(String username){
        trainer.getUser().setUsername(username);
        return this;
    }

    public TrainerBuilder setFirstName(String firstName){
        trainer.getUser().setFirstName(firstName);
        return this;
    }

    public TrainerBuilder setLastName(String lastName){
        trainer.getUser().setLastName(lastName);
        return this;
    }

    public TrainerBuilder setActive(boolean isActive){
        trainer.getUser().setActive(isActive);
        return this;
    }

    public TrainerBuilder setPassword(String password){
        trainer.getUser().setPassword(password);
        return this;
    }

    public TrainerBuilder setSpecialization(TrainingType type){
        trainer.setSpecialization(type);
        return this;
    }


    public Trainer build(){
        return trainer;
    }
}
