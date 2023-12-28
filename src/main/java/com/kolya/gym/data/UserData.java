package com.kolya.gym.data;


public class UserData {
    private String firstName;
    private String lastName;

    public void isValid() throws IllegalArgumentException{
        if (firstName==null || firstName.isBlank()) throw new IllegalArgumentException("Wrong parameter 'firstName': can't be empty");
        if (lastName==null || lastName.isBlank()) throw new IllegalArgumentException("Wrong parameter 'lastName': can't be empty");
        isValidCharacters();
    }

    public void isValidCharacters() throws IllegalArgumentException{
        if (firstName!=null && !firstName.matches("[a-zA-Z]*")) throw new IllegalArgumentException("Wrong parameter 'firstName' = "+firstName+": forbidden symbols");
        if (lastName!=null && !lastName.matches("[a-zA-Z]*")) throw new IllegalArgumentException("Wrong parameter 'lastName' = "+lastName+": forbidden symbols");
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
