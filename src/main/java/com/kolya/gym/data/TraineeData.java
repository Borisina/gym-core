package com.kolya.gym.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TraineeData extends UserData{
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;

    public void validate() throws IllegalArgumentException{
        super.validate();
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

    @Override
    public String toString() {
        return "TraineeData{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", userData = "+super.toString()+'}';
    }
}
