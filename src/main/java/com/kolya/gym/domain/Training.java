package com.kolya.gym.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
public class Training{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="trainer_id")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name="trainee_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trainee trainee;
    private String trainingName;

    //@ElementCollection(targetClass = TrainingType.class, fetch = FetchType.EAGER)
    //@CollectionTable(name = "training_type", joinColumns = @JoinColumn(name = "training_id"))
    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;

    @Temporal(TemporalType.DATE)
    private Date trainingDate;
    private int duration;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
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

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainer=" + trainer +
                ", trainee=" + trainee +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", duration=" + duration +
                '}';
    }
}
