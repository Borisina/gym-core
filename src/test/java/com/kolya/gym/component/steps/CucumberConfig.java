package com.kolya.gym.component.steps;

import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.TrainingRepo;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;

@CucumberContextConfiguration
@SpringBootTest(properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberConfig {
    @MockBean
    private TrainerRepo trainerRepo;

    @MockBean
    private TraineeRepo traineeRepo;

    @MockBean
    private TrainingRepo trainingRepo;

    @MockBean
    private JmsTemplate jmsTemplate;
}

