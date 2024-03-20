package com.kolya.gym.steps;

import com.kolya.gym.repo.UserRepo;
import com.kolya.gym.service.JwtService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.HttpCookie;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LogoutSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ScenarioContext scenarioContext;



    @When("^the user tries to logout")
    public void the_user_tries_to_logout() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, "jwt="+scenarioContext.getJwt());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/logout", HttpMethod.POST, entity, String.class);
        scenarioContext.setResponse(response);
    }

    @Then("^jwt cookie should be expired$")
    public void the_user_should_get_a_jwt_cookie$() {
        List<String> list = scenarioContext.getResponse().getHeaders().get(HttpHeaders.SET_COOKIE);
        assertNotNull(list);
        Long maxAge = null;
        for (String setCookieHeader : list) {
            List<HttpCookie> cookies = HttpCookie.parse(setCookieHeader);
            for (HttpCookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    maxAge = cookie.getMaxAge();
                }
            }
        }
        assertEquals(0,maxAge);
    }
}
