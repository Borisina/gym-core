package com.kolya.gym.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.kolya.gym.domain.TrainerWorkload;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetWorkload {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ScenarioContext scenarioContext;

    private WireMockServer wireMockServer;

    @Before("@workload")
    public void setup() throws JsonProcessingException {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("MARCH",30);
        trainerWorkload.getWorkload().put(2020,monthMap);
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/trainer-workload/Michail.Kotov"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(new ObjectMapper().writeValueAsString(trainerWorkload))));
    }

    @After("@workload")
    public void stop() {
        wireMockServer.stop();
    }

    @When("^the user tries to get workload by trainer's username \"([^\"]*)\"$")
    public void the_user_tries_to_get_workload(String trainerUsername) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, "jwt="+scenarioContext.getJwt());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/trainers/"+trainerUsername+"/workload", HttpMethod.GET, entity, String.class);
        scenarioContext.setResponse(response);
    }


    @Then("^the user should get json workload info$")
    public void the_user_should_get_json_workload_info() {
        String body = scenarioContext.getResponse().getBody();
        Integer duration = null;
        try {
            TrainerWorkload trainerWorkload =new ObjectMapper().readValue(body, TrainerWorkload.class);
            duration = trainerWorkload.getWorkload().get(2020).get("MARCH");
        } catch (JsonProcessingException ignored) {}

        assertEquals(30, duration);
    }
}
