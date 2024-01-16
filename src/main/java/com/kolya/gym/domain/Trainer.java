package com.kolya.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kolya.gym.converter.TrainingTypeConverter;
import javax.persistence.*;

import java.util.List;

@Entity
public class Trainer{
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "training_type_id")
    @Convert(converter = TrainingTypeConverter.class)
    private TrainingType specialization;


    @JsonIgnoreProperties({"trainersList","dateOfBirth","address"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="trainees_trainers",
            joinColumns = {@JoinColumn(name="trainer_id")},
            inverseJoinColumns ={@JoinColumn(name="trainee_id")}
    )
    private List<Trainee> traineesList;

    @JsonIgnore
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private List<Training> trainingsList;

    @JsonUnwrapped
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Trainee> getTraineesList() {
        return traineesList;
    }

    public void setTraineesList(List<Trainee> traineesList) {
        this.traineesList = traineesList;
    }

    public List<Training> getTrainingsList() {
        return trainingsList;
    }

    public void setTrainingsList(List<Training> trainingsList) {
        this.trainingsList = trainingsList;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", specialization='" + specialization + '\'' +
                ", user=" + user +
                '}';
    }
}
