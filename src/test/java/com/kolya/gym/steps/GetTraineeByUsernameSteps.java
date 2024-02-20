package com.kolya.gym.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.builder.TraineeBuilder;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.UserRepo;
import com.kolya.gym.service.JwtService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTraineeByUsernameSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ScenarioContext scenarioContext;

    private String username;



    @Given("^a trainee with username \"([^\"]*)\", who's already in the database$")
    public void a_trainee_with_username(String username) {
        this.username = username;
        Trainee trainee = new TraineeBuilder()
                .setUsername(username)
                .setFirstName("first")
                .setLastName("last")
                .setPassword("password")
                .setActive(true)
                .build();
        traineeRepo.save(trainee);
    }


    @When("^the user tries to get trainee info by username$")
    public void the_user_tries_to_get_trainee_info_by_username() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, "jwt="+scenarioContext.getJwt());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/trainees/"+username, HttpMethod.GET, entity, String.class);
        scenarioContext.setResponse(response);
    }


    @Then("^the user should get json info about trainee$")
    public void the_user_should_get_json_info_about_trainee() {
        String body = scenarioContext.getResponse().getBody();
        String traineeUsernameFromJson = null;
        try {
            Trainee trainee =new ObjectMapper().readValue(body, Trainee.class);
            traineeUsernameFromJson = trainee.getUser().getUsername();
        } catch (JsonProcessingException ignored) {}

        assertEquals(username, traineeUsernameFromJson);
    }
}
