package com.kolya.gym.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingCriteria {
    private String trainingName;
    private TrainingType trainingType;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date trainingDateFrom;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date trainingDateTo;

    public void validate() throws IllegalArgumentException{
        if (trainingDateFrom!=null && trainingDateTo!=null && trainingDateFrom.after(trainingDateTo)) throw new IllegalArgumentException("Wrong parameters: trainingDateTo<trainingDateFrom");
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

    public Date getTrainingDateFrom() {
        return trainingDateFrom;
    }

    public void setTrainingDateFrom(Date trainingDateFrom) {
        this.trainingDateFrom = trainingDateFrom;
    }

    public Date getTrainingDateTo() {
        return trainingDateTo;
    }

    public void setTrainingDateTo(Date trainingDateTo) {
        this.trainingDateTo = trainingDateTo;
    }
}
