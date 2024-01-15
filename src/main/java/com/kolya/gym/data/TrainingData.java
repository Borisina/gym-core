package com.kolya.gym.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingData {
    private String trainerUsername;
    private String traineeUsername;
    private String trainingName;
    private TrainingType trainingType;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date trainingDate;
    private int duration;


    public void validate() throws IllegalArgumentException{
        if (trainingName==null || trainingName.isBlank()) throw new IllegalArgumentException("Wrong parameter 'trainingName': can't be empty");
        if (trainingType==null) throw new IllegalArgumentException("Wrong parameter 'trainingType': can't be null");
        if (trainingDate==null) throw new IllegalArgumentException("Wrong parameter 'trainingDate': can't be empty");
        if (duration<=0) throw new IllegalArgumentException("Wrong parameter 'duration' = "+duration+": can't be <=0");
    }


    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
