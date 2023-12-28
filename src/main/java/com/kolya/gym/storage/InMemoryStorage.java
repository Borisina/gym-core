package com.kolya.gym.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.domain.*;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStorage {



    private Map<Long, User> users = new HashMap<>();
    private Map<Long, Trainee> trainees = new HashMap<>();
    private Map<Long, Trainer> trainers = new HashMap<>();
    private Map<Long, Training> trainings = new HashMap<>();

    private long traineeCounter = 0;
    private long trainerCounter = 0;
    private long trainingCounter = 0;
    private long userCounter = 0;

    @JsonIgnore
    @Value("${storage.fill.file}")
    private String filePath;
    @JsonIgnore
    private Logger logger;


    @PostConstruct
    public void fill() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InMemoryStorage inMemoryStorageFromFile = mapper.readValue(new File(filePath), InMemoryStorage.class);
        this.traineeCounter = inMemoryStorageFromFile.traineeCounter;
        this.trainerCounter = inMemoryStorageFromFile.trainerCounter;
        this.userCounter = inMemoryStorageFromFile.userCounter;
        this.trainingCounter = inMemoryStorageFromFile.trainingCounter;
        this.users = inMemoryStorageFromFile.users;
        this.trainers = inMemoryStorageFromFile.trainers;
        this.trainees = inMemoryStorageFromFile.trainees;
        this.trainings = inMemoryStorageFromFile.trainings;
        logger.info("InMemoryStorage is filled with data from file: " + filePath );

        /*Trainee trainee = new Trainee();
        User user = new User();
        user.setId(1L);
        user.setUsername("Vasya.Utkin");
        user.setPassword("1234567890");
        user.setFirstName("Vasya");
        user.setLastName("Vasya");
        trainee.setUser(user);
        trainee.setId(1L);
        Map<Long,User> users = new HashMap<>();
        users.put(1L, user);
        Map<Long,Trainee> trainees = new HashMap<>();
        trainees.put(1L, trainee);
        InMemoryStorage inMemoryStorage= new InMemoryStorage();
        inMemoryStorage.trainees = trainees;
        inMemoryStorage.traineeCounter = 1;
        inMemoryStorage.trainers = new HashMap<>();
        inMemoryStorage.users = users;
        inMemoryStorage.userCounter = 1;
        inMemoryStorage.trainings = new HashMap<>();
        try {
            // Writing to a file
            mapper.writeValue(new File("D:\\Epam\\UzJava\\spring-core\\gym\\myfile.json"), inMemoryStorage );

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public Map<Long, Trainee> getTrainees() {
        return trainees;
    }

    public Map<Long, Trainer> getTrainers() {
        return trainers;
    }

    public Map<Long, Training> getTrainings() {
        return trainings;
    }

    @JsonIgnore
    public long getNewTraineeId(){
        traineeCounter++;
        return traineeCounter;
    }

    @JsonIgnore
    public long getNewTrainerId(){
        trainerCounter++;
        return trainerCounter;
    }

    @JsonIgnore
    public long getNewTrainingId(){
        trainingCounter++;
        return trainingCounter;
    }

    @JsonIgnore
    public long getNewUserId(){
        userCounter++;
        return userCounter;
    }

    @Autowired
    public void setLogger(Logger logger){
        this.logger = logger;
    }
}
