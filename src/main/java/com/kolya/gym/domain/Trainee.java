package com.kolya.gym.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Trainee {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;

    @JsonIgnoreProperties({"traineesList"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="trainees_trainers",
            joinColumns = {@JoinColumn(name="trainee_id")},
            inverseJoinColumns ={@JoinColumn(name="trainer_id")}
    )
    private List<Trainer> trainersList;

    @JsonIgnore
    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY)
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Trainer> getTrainersList() {
        return trainersList;
    }

    public void setTrainersList(List<Trainer> trainersList) {
        this.trainersList = trainersList;
    }

    public List<Training> getTrainingsList() {
        return trainingsList;
    }

    public void setTrainingsList(List<Training> trainingsList) {
        this.trainingsList = trainingsList;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}
