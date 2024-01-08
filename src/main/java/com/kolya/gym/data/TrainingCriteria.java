package com.kolya.gym.data;

import com.kolya.gym.domain.TrainingType;

import java.util.Date;

public class TrainingCriteria {
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDateFrom;
    private Date trainingDateTo;
    private Integer durationMin;
    private Integer durationMax;

    public void validate() throws IllegalArgumentException{
        if (durationMin!=null && durationMin<0) throw new IllegalArgumentException("Wrong parameter durationMin: cant be negative ");
        if (durationMax!=null && durationMax<0) throw new IllegalArgumentException("Wrong parameter durationMax: cant be negative ");
        if (durationMin!=null && durationMax!=null && durationMin>durationMax) throw new IllegalArgumentException("Wrong parameters: durationMax<durationMin");
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

    public Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }

    public Integer getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(Integer durationMax) {
        this.durationMax = durationMax;
    }
}
