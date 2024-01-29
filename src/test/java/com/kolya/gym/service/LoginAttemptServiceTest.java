package com.kolya.gym.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptServiceTest {
    public static final String TEST_KEY = "key1";

    @InjectMocks
    private LoginAttemptService service;

    int MAX_ATTEMPT = 3;

    @Before
    public void setUp(){
        service.postConstruct();
    }


    @Test
    public void whenLoginFailed_thenAttemptsCacheCountIncreases() {
        service.loginSucceeded(TEST_KEY);
        service.loginFailed(TEST_KEY);
        assertTrue(service.isBlocked(TEST_KEY));
    }

    @Test
    public void whenLoginFailedAfterSuccess_thenIsStillBlocked() {
        service.loginSucceeded(TEST_KEY);
        service.loginFailed(TEST_KEY);
        service.loginSucceeded(TEST_KEY);

        assertTrue(service.isBlocked(TEST_KEY));
    }

    @Test
    public void whenFailedMoreThanMaxAttempt_thenIsBlocked() {
        for (int i = 0; i < MAX_ATTEMPT; i++) {
            service.loginFailed(TEST_KEY);
        }

        assertTrue(service.isBlocked(TEST_KEY));
    }
}