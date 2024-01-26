package com.kolya.gym.service;

import com.kolya.gym.builder.TrainerWorkloadRequestDataBuilder;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.domain.Training;
import com.kolya.gym.feign.FeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TrainerWorkloadService {

    private final Logger logger = LoggerFactory.getLogger(TrainerWorkloadService.class);

    @Autowired
    private FeignClient feignClient;
    @Autowired
    private JwtService jwtService;

    private String jwtBearerToken;

    @HystrixCommand(fallbackMethod = "fallbackAddTraining")
    public void addTraining(UUID transactionId, TrainerWorkloadRequestData requestData) {
        logger.info("Transaction ID: {}, Try to do request POST /trainer-workload-service/trainer-workload with data: {}", transactionId, requestData);
        jwtBearerToken = jwtService.getTokenForServices(transactionId);
        ResponseEntity<String> response = feignClient.addTraining(jwtBearerToken, requestData);
        logger.info("Transaction ID: {}, Request POST /trainer-workload-service/trainer-workload returned status {}", transactionId, response.getStatusCode());
    }

    @HystrixCommand(fallbackMethod = "fallbackDeleteTraining")
    public void deleteTraining(UUID transactionId,  TrainerWorkloadRequestData requestData) {
        logger.info("Transaction ID: {}, Try to do request DELETE /trainer-workload-service/trainer-workload with data: {}", transactionId, requestData);
        jwtBearerToken = jwtService.getTokenForServices(transactionId);
        ResponseEntity<String> response = feignClient.deleteTraining(jwtBearerToken, requestData);
        logger.info("Transaction ID: {}, Request DELETE /trainer-workload-service/trainer-workload returned status {}", transactionId, response.getStatusCode());
    }

    @HystrixCommand(fallbackMethod = "fallbackGetTrainingWorkload")
    public TrainerWorkload getTrainerWorkload(UUID transactionId, String username) throws ServiceUnavailableException {
        logger.info("Transaction ID: {}, Try to do request GET /trainer-workload-service/trainer-workload/{}", transactionId, username);
        jwtBearerToken = jwtService.getTokenForServices(transactionId);
        TrainerWorkload trainerWorkload = feignClient.getTrainerWorkload(jwtBearerToken, username);
        logger.info("Transaction ID: {}, Request GET /trainer-workload-service returned trainerWorkload: {}", transactionId, trainerWorkload);
        return trainerWorkload;
    }

    public void deleteTrainings(UUID transactionId, Set<Training> trainingSet){
        if (trainingSet!=null){
            Date now = new Date();
            trainingSet.stream()
                    .filter(training -> training.getTrainingDate().after(now))
                    .forEach(training -> deleteTraining(transactionId, getRequestDataFromTraining(training)));
        }
    }

    public void fallbackAddTraining(UUID transactionId, TrainerWorkloadRequestData requestData, Throwable hystrixCommand) {
        logger.error("Transaction ID: {}, Tried to send request POST /trainer-workload-service/trainer-workload with body {}, but got error: {}" ,
                transactionId, requestData, hystrixCommand.getMessage());
    }

    public void fallbackDeleteTraining(UUID transactionId, TrainerWorkloadRequestData requestData, Throwable hystrixCommand) {
        logger.error("Transaction ID: {}, Tried to send request DELETE /trainer-workload-service/trainer-workload with with body {}, but got error: {}" ,
                transactionId, requestData, hystrixCommand.getMessage());
    }

    public TrainerWorkload fallbackGetTrainingWorkload(UUID transactionId, String  username, Throwable hystrixCommand) throws ServiceUnavailableException {
        logger.error("Transaction ID: {}, Tried to send request GET /trainer-workload-service/trainer-workload/{}, but got error: {}" ,
                transactionId, username, hystrixCommand.getMessage());
        throw new ServiceUnavailableException("Sorry, Service is unavailable now.");
    }

    public TrainerWorkloadRequestData getRequestDataFromTraining(Training training){
        Trainer trainer = training.getTrainer();
        TrainerWorkloadRequestData requestData = new TrainerWorkloadRequestDataBuilder()
                .setUsername(trainer.getUser().getUsername())
                .setFirstName(trainer.getUser().getFirstName())
                .setLastName(trainer.getUser().getLastName())
                .setTrainingDate(training.getTrainingDate())
                .setActive(trainer.getUser().isActive())
                .setTrainingDuration(training.getDuration())
                .build();
        return requestData;
    }
}
