package com.kolya.gym.data;


public class UserData {
    private String firstName;
    private String lastName;

    public void validate() throws IllegalArgumentException{
        if (firstName==null)  throw new IllegalArgumentException("Wrong parameter 'firstName': can't be null");
        if (lastName==null) throw new IllegalArgumentException("Wrong parameter 'lastName': can't be null");
        validateCharacters();
    }

    public void validateCharacters() throws IllegalArgumentException{
        if (firstName!=null){
            if (firstName.isBlank()) throw new IllegalArgumentException("Wrong parameter 'firstName': can't be empty");
            if (firstName!=null && !firstName.matches("[a-zA-Z]*")) throw new IllegalArgumentException("Wrong parameter 'firstName' = "+firstName+": forbidden symbols");
        }
        if (firstName!=null){
            if (lastName.isBlank()) throw new IllegalArgumentException("Wrong parameter 'lastName': can't be empty");
            if (lastName!=null && !lastName.matches("[a-zA-Z]*")) throw new IllegalArgumentException("Wrong parameter 'lastName' = "+lastName+": forbidden symbols");
        }
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
