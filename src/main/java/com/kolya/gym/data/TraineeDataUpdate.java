package com.kolya.gym.data;

public class TraineeDataUpdate extends TraineeData{
    long id;

    @Override
    public void validate() throws IllegalArgumentException {
        if (id<=0) throw new IllegalArgumentException("Trainee id can't be <=0");
        super.validateCharacters();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
