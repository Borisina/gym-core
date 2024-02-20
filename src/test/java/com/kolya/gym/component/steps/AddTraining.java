package com.kolya.gym.component.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.builder.TraineeBuilder;
import com.kolya.gym.builder.TrainerBuilder;
import com.kolya.gym.builder.TrainingDataBuilder;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.data.TrainingData;
import com.kolya.gym.domain.Trainee;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.Training;
import com.kolya.gym.domain.TrainingType;
import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.TrainingRepo;
import com.kolya.gym.service.JwtService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddTraining {

    @Autowired
    private TrainerRepo trainerRepo;

    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private TrainingRepo trainingRepo;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScenarioComponentContext scenarioContext;

    private TrainingData trainingData;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    @Given("^a training data with traineeUsername \"([^\"]*)\", trainerUsername \"([^\"]*)\", duration ([0-9]*), date \"([^\"]*)\" and type \"([^\"]*)\"$")
    public void a_training_data(String traineeUsername, String trainerUsername, int duration, String dateStr, String trainingType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = formatter.parse(dateStr);
        trainingData = new TrainingDataBuilder()
                .setTraineeUsername(traineeUsername)
                .setTrainerUsername(trainerUsername)
                .setDuration(duration)
                .setTrainingDate(date)
                .setTrainingType(TrainingType.valueOf(trainingType))
                .build();
        Trainee trainee = new TraineeBuilder()
                .setPassword("password")
                .setUsername(traineeUsername)
                .setFirstName("first")
                .setLastName("last")
                .setActive(true)
                .build();
        trainee.setTrainersSet(new HashSet<>());
        when(traineeRepo.findByUserUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        Trainer trainer = new TrainerBuilder()
                .setPassword("password")
                .setUsername(trainerUsername)
                .setSpecialization(TrainingType.valueOf(trainingType))
                .setFirstName("first")
                .setLastName("last")
                .setActive(true)
                .build();
        trainer.setTraineesSet(new HashSet<>());
        when(trainerRepo.findByUserUsername(trainerUsername)).thenReturn(Optional.of(trainer));
    }


    @When("^the user tries to add training$")
    public void the_user_tries_to_add_training() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, "jwt="+scenarioContext.getJwt());
        String json = new ObjectMapper().writeValueAsString(trainingData);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange("/trainings", HttpMethod.POST, entity, String.class);
        scenarioContext.setResponse(response);
    }

    @Then("^the training should be saved in the database$")
    public void the_training_should_be_saved_in_the_database() {
        verify(trainingRepo,times(1)).save(any(Training.class));
    }

    @Then("^a message should be added to the queue$")
    public void a_message_should_be_added_to_the_queue() throws JsonProcessingException {
        verify(jmsTemplate,times(1)).convertAndSend(eq(QUEUE_NAME_WORKLOAD), any(TrainerWorkloadRequestData.class));
    }
}

