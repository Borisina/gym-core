package com.kolya.gym.data;

import java.util.Date;

public class TraineeData extends UserData{
    private Date dateOfBirth;
    private String Address;

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
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
