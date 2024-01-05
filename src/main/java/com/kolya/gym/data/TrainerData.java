package com.kolya.gym.data;


public class TrainerData extends UserData{
    private String specialization;

    public void validate() throws IllegalArgumentException{
        if (specialization==null || specialization.isBlank()) throw new IllegalArgumentException("Wrong parameter 'specialization': can't be empty");
        super.validate();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
