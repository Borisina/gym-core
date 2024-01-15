package com.kolya.gym.builder;

import com.kolya.gym.data.AuthData;

public class AuthDataBuilder {
    private final AuthData authData;

    public AuthDataBuilder() {
        authData = new AuthData();
    }

    public AuthDataBuilder setUsername(String username){
        authData.setUsername(username);
        return this;
    }

    public AuthDataBuilder setPassword(String password){
        authData.setPassword(password);
        return this;
    }

    public AuthData build(){
        return authData;
    }
}
