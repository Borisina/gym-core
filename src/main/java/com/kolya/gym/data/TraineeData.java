package com.kolya.gym.data;

import java.util.Date;

public class TraineeData extends UserData {
    private Date dateOfBirt;
    private String Address;

    public void isValid() throws IllegalArgumentException{
        super.isValid();
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
}
