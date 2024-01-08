package com.kolya.gym.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date dateOfBirth;
    private String Address;

    @OneToOne(cascade = CascadeType.ALL)
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
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", Address='" + Address + '\'' +
                ", user=" + user +
                '}';
    }
}
