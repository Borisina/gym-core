package com.kolya.gym.data;

public class AuthData {
    private String username;
    private String password;

    public AuthData() {
    }

    public AuthData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void validate() throws IllegalArgumentException{

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
