package com.kolya.gym.service;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.TrainerWorkload;
import com.kolya.gym.feign.FeignClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TrainerWorkloadServiceTest {

    @InjectMocks
    private TrainerWorkloadService workloadService;
    @Mock
    private JwtService jwtService;
    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private FeignClient feignClient;

    private String QUEUE_NAME_WORKLOAD;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException {
        QUEUE_NAME_WORKLOAD = "QUEUE_NAME_WORKLOAD";
        Field field = workloadService.getClass().getDeclaredField("QUEUE_NAME_WORKLOAD");
        field.setAccessible(true);
        field.set(workloadService, QUEUE_NAME_WORKLOAD);

    }

    @Test
    public void changeWorkloadTest() {
        UUID transactionId = UUID.randomUUID();
        TrainerWorkloadRequestData requestData = Mockito.mock(TrainerWorkloadRequestData.class);
        workloadService.changeWorkload(transactionId, requestData);
        Mockito.verify(jmsTemplate,Mockito.times(1)).convertAndSend(QUEUE_NAME_WORKLOAD, requestData);
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