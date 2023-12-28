package com.kolya.gym.domain;

import java.util.Date;

public class Trainee {
    private long id;
    private Date dateOfBirt;
    private String Address;
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOfBirt() {
        return dateOfBirt;
    }

    public void setDateOfBirt(Date dateOfBirt) {
        this.dateOfBirt = dateOfBirt;
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
                ", dateOfBirt=" + dateOfBirt +
                ", Address='" + Address + '\'' +
                ", user=" + user +
                '}';
    }
}
