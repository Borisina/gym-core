package com.kolya.gym.data;


public class TrainerData extends UserData {
    private String specialization;

    public void isValid() throws IllegalArgumentException{
        if (specialization==null || specialization.isBlank()) throw new IllegalArgumentException("Wrong parameter 'specialization': can't be empty");
        super.isValid();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
