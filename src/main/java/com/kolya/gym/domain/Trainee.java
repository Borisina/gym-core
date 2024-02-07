package com.kolya.gym.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Trainee {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;

    @JsonIgnoreProperties({"traineesSet"})
    @ManyToMany(mappedBy = "traineesSet")
    private Set<Trainer> trainersSet;

    @JsonIgnore
    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY)
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

    public Set<Trainer> getTrainersSet() {
        return trainersSet;
    }

    public void setTrainersSet(Set<Trainer> trainersSet) {
        this.trainersSet = trainersSet;
    }

    public Set<Training> getTrainingsSet() {
        return trainingsSet;
    }

    public void setTrainingsSet(Set<Training> trainingsSet) {
        this.trainingsSet = trainingsSet;
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
