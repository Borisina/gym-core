package com.kolya.gym.component.steps;


import com.kolya.gym.domain.User;
import com.kolya.gym.service.JwtService;
import com.kolya.gym.steps.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {
    @Autowired
    ScenarioComponentContext scenarioContext;

    @Autowired
    private JwtService jwtService;

    @Given("^a user, who's already logined and got his jwt$")
    public void a_user_with_jwt$() {
        User user = new User();
        user.setActive(true);
        user.setUsername("username");
        user.setPassword("password");
        UUID uuid = UUID.randomUUID();
        String jwt = jwtService.generateToken(uuid, user);
        scenarioContext.setJwt(jwt);
    }

    @Then("^response status should be ([0-9]*)$")
    public void response_status_should_be(int responseStatus) {
        assertEquals(responseStatus, scenarioContext.getResponse().getStatusCodeValue());
    }
}
