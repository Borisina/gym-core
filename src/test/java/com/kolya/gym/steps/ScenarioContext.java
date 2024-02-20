package com.kolya.gym.steps;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ScenarioContext {
    private ResponseEntity<String> response;
    private String jwt;

    public ResponseEntity<String> getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity<String> response) {
        this.response = response;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
