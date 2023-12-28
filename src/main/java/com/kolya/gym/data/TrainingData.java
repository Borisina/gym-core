package com.kolya.gym.data;

import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingData {
    private long trainerId;
    private long traineeId;
    private String trainingName;
    private String trainingType;
    private Date trainingDate;
    private int duration;


    public void isValid() throws IllegalArgumentException{
        if (trainerId<=0) throw new IllegalArgumentException("Wrong parameter 'trainerId' = "+trainerId+": can't be <=0");
        if (traineeId<=0) throw new IllegalArgumentException("Wrong parameter 'trainerId' = "+traineeId+": can't be <=0");
        if (trainingName==null || trainingName.isBlank()) throw new IllegalArgumentException("Wrong parameter 'trainingName': can't be empty");
        if (trainingType==null || trainingType.isBlank()) throw new IllegalArgumentException("Wrong parameter 'trainingType': should be 'type_1','type_2','type_3'");
        try{
            TrainingType.valueOf(trainingType.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Wrong parameter 'trainingType' = "+trainingType +": should be 'type_1','type_2','type_3'");
        }
        if (trainingDate==null) throw new IllegalArgumentException("Wrong parameter 'trainingDate': can't be empty");
        if (duration<=0) throw new IllegalArgumentException("Wrong parameter 'duration' = "+duration+": can't be <=0");
    }


    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(long traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
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
