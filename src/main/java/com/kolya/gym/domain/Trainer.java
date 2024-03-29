package com.kolya.gym.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kolya.gym.converter.TrainingTypeConverter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name="trainees_trainers",
            joinColumns = {@JoinColumn(name="trainer_id")},
            inverseJoinColumns ={@JoinColumn(name="trainee_id")}
    )
    private Set<Trainee> traineesSet;

    @JsonIgnore
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private Set<Training> trainingsSet;

    @JsonUnwrapped
    @OneToOne(cascade = CascadeType.PERSIST)
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

    public Set<Trainee> getTraineesSet() {
        return traineesSet;
    }

    public void setTraineesSet(Set<Trainee> traineesSet) {
        this.traineesSet = traineesSet;
    }

    public Set<Training> getTrainingsSet() {
        return trainingsSet;
    }

    public void setTrainingsSet(Set<Training> trainingsSet) {
        this.trainingsSet = trainingsSet;
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
