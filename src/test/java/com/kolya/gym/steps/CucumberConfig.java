package com.kolya.gym.steps;

import com.kolya.gym.repo.TraineeRepo;
import com.kolya.gym.repo.TrainerRepo;
import com.kolya.gym.repo.TrainingRepo;
import com.kolya.gym.repo.UserRepo;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

@CucumberContextConfiguration
@SpringBootTest(properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberConfig {

    @Autowired
    private TraineeRepo traineeRepo;

    @Autowired
    private TrainerRepo trainerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TrainingRepo trainingRepo;

    private JmsTemplate jmsTemplate;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    @Before
    @After
    public void deleteTrainers(){
        traineeRepo.deleteAll();
        trainingRepo.deleteAll();
        trainerRepo.deleteAll();
        userRepo.deleteAll();
    }

    @After("@queueTest")
    @Before("@queueTest")
    public void before(){
        clearQueue();
    }

    private void clearQueue() {
        while (true) {
            Object message = jmsTemplate.receive(QUEUE_NAME_WORKLOAD);
            if (message == null) {
                break;
            }
        }
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate){
        int receiveTimeout = 500;
        jmsTemplate.setReceiveTimeout(receiveTimeout);
        this.jmsTemplate = jmsTemplate;
    }
}