package com.kolya.gym.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.data.AuthData;
import com.kolya.gym.domain.User;
import com.kolya.gym.repo.UserRepo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ScenarioContext scenarioContext;

    private AuthData authData;



    @Given("^a user with username \"([^\"]*)\" and password \"([^\"]*)\", who's already registered$")
    public void a_user_with_username_and_password_whos_already_registered(String username, String password) {
        authData = new AuthData(username, password);
        User user = new User();
        user.setActive(true);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }

    @When("^the user tries to login")
    public void the_user_tries_to_login() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = new ObjectMapper().writeValueAsString(authData);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String > response = restTemplate.exchange("/login", HttpMethod.POST, entity, String.class);
        scenarioContext.setResponse(response);
    }


    @Then("^the user should get a jwt cookie$")
    public void the_user_should_get_a_jwt_cookie$() {
        List<String> list = scenarioContext.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE);
        assertNotNull(list);
        String jwt = null;
        if (!list.isEmpty()){
            jwt = list.get(0);
        }
        assertNotNull(jwt);
    }
}
