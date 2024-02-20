package com.kolya.gym.builder;

import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.User;

import java.util.Date;

public class TraineeBuilder {
    private Trainee trainee;

    public TraineeBuilder(){
        trainee = new Trainee();
        trainee.setUser(new User());
    }

    public TraineeBuilder setUsername(String username){
        trainee.getUser().setUsername(username);
        return this;
    }

    public TraineeBuilder setFirstName(String firstName){
        trainee.getUser().setFirstName(firstName);
        return this;
    }

    public TraineeBuilder setLastName(String lastName){
        trainee.getUser().setLastName(lastName);
        return this;
    }

    public TraineeBuilder setActive(boolean isActive){
        trainee.getUser().setActive(isActive);
        return this;
    }

    public TraineeBuilder setPassword(String password){
        trainee.getUser().setPassword(password);
        return this;
    }

    public TraineeBuilder setAddress(String address){
        trainee.setAddress(address);
        return this;
    }

    public TraineeBuilder setDateOfBirth(Date date){
        trainee.setDateOfBirth(date);
        return this;
    }


    public Trainee build(){
        return trainee;
    }
}
