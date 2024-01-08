package com.kolya.gym.domain;

import jakarta.persistence.*;

@Entity
public class Trainer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String specialization;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
