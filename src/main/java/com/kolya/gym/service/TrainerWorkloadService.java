package com.kolya.gym.service;

import com.kolya.gym.builder.TrainerWorkloadRequestDataBuilder;
import com.kolya.gym.data.ActionType;
import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.Trainer;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.domain.Training;
import com.kolya.gym.feign.FeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class TrainerWorkloadService {

    private final Logger logger = LoggerFactory.getLogger(TrainerWorkloadService.class);

    @Autowired
    private FeignClient feignClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JwtService jwtService;

    private String jwtBearerToken;

    @Value("${mq.queue.name.workload}")
    private String QUEUE_NAME_WORKLOAD;

    public void changeWorkload(UUID transactionId, TrainerWorkloadRequestData requestData) {
        logger.info("Transaction ID: {}, Try to do send message to the '{}'with data: {}", QUEUE_NAME_WORKLOAD, transactionId, requestData);
        jmsTemplate.convertAndSend(QUEUE_NAME_WORKLOAD, requestData);
        logger.info("Transaction ID: {}, Message sent to '{}'", transactionId, QUEUE_NAME_WORKLOAD);
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
        Date now = new Date();
        CollectionUtils.emptyIfNull(trainingSet).stream()
                .filter(training -> training.getTrainingDate().after(now))
                .forEach(training -> changeWorkload(transactionId, getRequestDataFromTraining(training,ActionType.DELETE)));
    }

    public TrainerWorkload fallbackGetTrainingWorkload(UUID transactionId, String  username, Throwable hystrixCommand) throws ServiceUnavailableException {
        logger.error("Transaction ID: {}, Tried to send request GET /trainer-workload-service/trainer-workload/{}, but got error: {}" ,
                transactionId, username, hystrixCommand.getMessage());
        throw new ServiceUnavailableException("Sorry, Service is unavailable now.");
    }

    public TrainerWorkloadRequestData getRequestDataFromTraining(Training training, ActionType actionType){
        Trainer trainer = training.getTrainer();
        TrainerWorkloadRequestData requestData = new TrainerWorkloadRequestDataBuilder()
                .setUsername(trainer.getUser().getUsername())
                .setFirstName(trainer.getUser().getFirstName())
                .setLastName(trainer.getUser().getLastName())
                .setTrainingDate(training.getTrainingDate())
                .setActive(trainer.getUser().isActive())
                .setTrainingDuration(training.getDuration())
                .setActionType(actionType)
                .build();
        return requestData;
    }
}
