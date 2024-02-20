package com.kolya.gym.component.steps;

import com.kolya.gym.domain.User;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.TrainingRepo;
import com.kolya.gym.repo.UserRepo;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;

import javax.sql.DataSource;

@CucumberContextConfiguration
@SpringBootTest(properties = "spring.profiles.active=c-test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberConfig {
    @MockBean
    private TrainerRepo trainerRepo;

    @MockBean
    private TraineeRepo traineeRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private TrainingRepo trainingRepo;

    @MockBean
    private JmsTemplate jmsTemplate;

    @MockBean
    private DataSource dataSource;
}

