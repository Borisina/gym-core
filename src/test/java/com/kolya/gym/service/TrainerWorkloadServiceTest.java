package com.kolya.gym.service;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.feign.FeignClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TrainerWorkloadServiceTest {

    @InjectMocks
    private TrainerWorkloadService workloadService;
    @Mock
    private JwtService jwtService;
    @Mock
    private FeignClient feignClient;

    @Test
    public void addTrainingTest() {
        UUID transactionId = UUID.randomUUID();
        TrainerWorkloadRequestData requestData = Mockito.mock(TrainerWorkloadRequestData.class);
        String jwtBearerToken = "Bearer jwtTest";
        ResponseEntity<String> response = ResponseEntity.ok("Training added");

        Mockito.when(jwtService.getTokenForServices(transactionId)).thenReturn(jwtBearerToken);
        Mockito.when(feignClient.addTraining(jwtBearerToken, requestData)).thenReturn(response);

        workloadService.addTraining(transactionId, requestData);

        Mockito.verify(jwtService).getTokenForServices(transactionId);
        Mockito.verify(feignClient).addTraining(jwtBearerToken, requestData);
    }

    @Test
    public void deleteTrainingTest() {
        UUID transactionId = UUID.randomUUID();
        TrainerWorkloadRequestData requestData = Mockito.mock(TrainerWorkloadRequestData.class);
        String jwtBearerToken = "Bearer jwtTest";
        ResponseEntity<String> response = ResponseEntity.ok("Training deleted");

        Mockito.when(jwtService.getTokenForServices(transactionId)).thenReturn(jwtBearerToken);
        Mockito.when(feignClient.deleteTraining(jwtBearerToken, requestData)).thenReturn(response);

        workloadService.deleteTraining(transactionId, requestData);

        Mockito.verify(jwtService).getTokenForServices(transactionId);
        Mockito.verify(feignClient).deleteTraining(jwtBearerToken, requestData);
    }

    @Test
    public void getTrainerWorkloadTest() throws Exception {
        UUID transactionId = UUID.randomUUID();
        String username = "trainer";
        String jwtBearerToken = "Bearer jwtTest";
        TrainerWorkload workload = Mockito.mock(TrainerWorkload.class);

        Mockito.when(jwtService.getTokenForServices(transactionId)).thenReturn(jwtBearerToken);
        Mockito.when(feignClient.getTrainerWorkload(jwtBearerToken, username)).thenReturn(workload);

        TrainerWorkload result = workloadService.getTrainerWorkload(transactionId, username);

        Mockito.verify(jwtService).getTokenForServices(transactionId);
        Mockito.verify(feignClient).getTrainerWorkload(jwtBearerToken, username);
        assertEquals(workload, result);
    }

}