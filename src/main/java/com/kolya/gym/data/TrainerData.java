package com.kolya.gym.data;


import com.kolya.gym.domain.TrainingType;


public class TrainerData extends UserData{
    private TrainingType specialization;

    public void validate() throws IllegalArgumentException{
        if(specialization==null){
            throw new IllegalArgumentException("Parameter 'specialization' is required");
        }
        super.validate();
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return "TrainerData{" +
                "specialization=" + specialization +
                ", userData = "+super.toString()+'}';
    }
}
