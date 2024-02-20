package com.kolya.gym.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.builder.TrainerDataBuilder;
import com.kolya.gym.data.TrainerData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.TrainingType;
import com.kolya.gym.repo.TrainerRepo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;


public class TrainerRegistrationSteps {

    @Autowired
    private TrainerRepo trainerRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScenarioContext scenarioContext;
    private String bearerToken;

    private TrainerData trainerData;


    @Given("^a trainer with firstName \"([^\"]*)\", lastName \"([^\"]*)\", specialization \"([^\"]*)\" and bearerToken \"([^\"]*)\" for registration$")
    public void a_trainer_with_firstName_lastName_and_specialization(String firstName, String lastName, String specialization, String bearerToken) {
        trainerData = new TrainerDataBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setSpecialization(TrainingType.valueOf(specialization))
                .build();
        this.bearerToken = bearerToken;
    }

    @When("^the trainer tries to register$")
    public void the_trainer_tries_to_register() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new ObjectMapper().writeValueAsString(trainerData);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/trainers", HttpMethod.POST, entity, String.class);
        scenarioContext.setResponse(response);
    }

    @Then("^the trainer should be saved in the database$")
    public void the_trainer_should_be_saved_in_the_database() {
        Trainer found = trainerRepo.findByUserUsername(trainerData.getFirstName()+"."+trainerData.getLastName()).orElse(null);
        assertNotNull(found);
    }

    @Then("^the trainer shouldn't be saved in the database$")
    public void the_trainer_should_not_be_saved_in_the_database() {
        Trainer found = trainerRepo.findByUserUsername(trainerData.getFirstName()+"."+trainerData.getLastName()).orElse(null);
        assertNull(found);
    }
}