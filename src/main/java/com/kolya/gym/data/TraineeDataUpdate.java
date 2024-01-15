package com.kolya.gym.data;


public class TraineeDataUpdate extends TraineeData{
    private String username;

    @Override
    public void validate() throws IllegalArgumentException {
        if (username==null || username.isBlank()) throw new IllegalArgumentException("Username cant be empty.");
        if (isActive()==null) throw new IllegalArgumentException("isActive is required");
        super.validateCharacters();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "TraineeDataUpdate{" +
                "username='" + username + '\'' +
                "traineeData = "+super.toString()+'}';
    }
}
