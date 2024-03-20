package com.kolya.gym.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.builder.TraineeDataBuilder;
import com.kolya.gym.data.TraineeData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.repo.TraineeRepo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;


public class TraineeRegistrationSteps {

    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScenarioContext scenarioContext;

    private String bearerToken;

    private TraineeData traineeData;



    @Given("^a trainee with firstName \"([^\"]*)\", lastName \"([^\"]*)\" and bearerToken \"([^\"]*)\" for registration$")
    public void a_trainee_with_firstName_and_lastName(String firstName, String lastName, String bearerToken) {
        traineeData = new TraineeDataBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();
        this.bearerToken = bearerToken;
    }

    @When("^the trainee tries to register$")
    public void the_trainee_tries_to_register() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new ObjectMapper().writeValueAsString(traineeData);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/trainees", HttpMethod.POST, entity, String.class);
        scenarioContext.setResponse(response);
    }

    @Then("^the trainee should be saved in the database$")
    public void the_trainee_should_be_saved_in_the_database() {
        Trainee found = traineeRepo.findByUserUsername(traineeData.getFirstName()+"."+traineeData.getLastName()).orElse(null);
        assertNotNull(found);
    }

    @Then("^the trainee shouldn't be saved in the database$")
    public void the_trainee_should_not_be_saved_in_the_database() {
        Trainee found = traineeRepo.findByUserUsername(traineeData.getFirstName()+"."+traineeData.getLastName()).orElse(null);
        assertNull(found);
    }
}