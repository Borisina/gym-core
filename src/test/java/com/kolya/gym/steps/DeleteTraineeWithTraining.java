package com.kolya.gym.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.builder.TraineeBuilder;
import com.kolya.gym.builder.TrainerBuilder;
import com.kolya.gym.builder.TrainingDataBuilder;
import com.kolya.gym.data.ActionType;
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
import com.kolya.gym.service.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteTraineeWithTraining {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrainerRepo trainerRepo;

    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private TrainingRepo trainingRepo;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private TrainingService trainingService;

    private String traineeUsername;
    private String trainerUsername;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    @Given("^a trainee with username \"([^\"]*)\"$")
    public void a_trainee_with_username(String traineeUsername){
        this.traineeUsername = traineeUsername;
        Trainee trainee = new TraineeBuilder()
                .setPassword("password")
                .setUsername(traineeUsername)
                .setFirstName("first")
                .setLastName("last")
                .setActive(true)
                .build();
        traineeRepo.save(trainee);
    }

    @Given("^a trainer with username \"([^\"]*)\"$")
    public void a_trainer_with_username(String trainerUsername){
        this.trainerUsername = trainerUsername;
        Trainer trainer = new TrainerBuilder()
                .setSpecialization(TrainingType.TYPE_1)
                .setActive(true)
                .setFirstName("first")
                .setLastName("last")
                .setUsername(trainerUsername)
                .setPassword("password")
                .build();
        trainerRepo.save(trainer);
    }

    @Transactional
    @Given("^trainee's training with the trainer, that is scheduled for next year$")
    public void training_next_year(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 1);
        Date date = c.getTime();
        TrainingData trainingData = new TrainingDataBuilder()
                .setTrainingDate(date)
                .setTrainingType(TrainingType.TYPE_1)
                .setTrainingName("name")
                .setTraineeUsername(traineeUsername)
                .setTrainerUsername(trainerUsername)
                .setDuration(20)
                .build();
        UUID uuid = UUID.randomUUID();
        trainingService.create(uuid,trainingData);
    }

    @Transactional
    @Given("^trainee's training with the trainer, that is passed last year$")
    public void training_last_year(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        Date date = c.getTime();
        TrainingData trainingData = new TrainingDataBuilder()
                .setTrainingDate(date)
                .setTrainingType(TrainingType.TYPE_1)
                .setTrainingName("name")
                .setTraineeUsername(traineeUsername)
                .setTrainerUsername(trainerUsername)
                .setDuration(20)
                .build();
        UUID uuid = UUID.randomUUID();
        trainingService.create(uuid,trainingData);
    }


    @When("^the user tries to delete trainee by username$")
    public void the_user_tries_to_delete_trainee_by_username() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.COOKIE, "jwt="+scenarioContext.getJwt());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/trainees/"+traineeUsername, HttpMethod.DELETE, entity, String.class);
        scenarioContext.setResponse(response);
    }

    @Then("^the trainee and the training should be deleted from the database$")
    public void the_training_and_the_training_should_be_deleted_from_the_database() {
        List<Training> trainings = trainingRepo.findAll();
        assertTrue(trainings.isEmpty());
        Trainee trainee = traineeRepo.findByUserUsername(traineeUsername).orElse(null);
        assertNull(trainee);
    }

    @Then("^a message should be added to the queue with action status \"DELETE\"$")
    public void a_message_should_be_added_to_the_queue(){
        jmsTemplate.receive(QUEUE_NAME_WORKLOAD);
        Object message = jmsTemplate.receiveAndConvert(QUEUE_NAME_WORKLOAD);
        TrainerWorkloadRequestData data = null;
        if (message instanceof TrainerWorkloadRequestData) {
            data = (TrainerWorkloadRequestData) message;
        }
        assertNotNull(data);
        assertEquals(trainerUsername, data.getUsername());
        assertEquals(ActionType.DELETE, data.getActionType());
    }

    @Then("^a message shouldn't be added to the queue$")
    public void a_message_should_not_be_added_to_the_queue(){
        jmsTemplate.receive(QUEUE_NAME_WORKLOAD);
        Object message = jmsTemplate.receiveAndConvert(QUEUE_NAME_WORKLOAD);
        assertNull(message);
    }
}
