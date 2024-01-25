package com.kolya.gym.feign;

import com.kolya.gym.data.TrainerWorkloadRequestData;
import com.kolya.gym.domain.TrainerWorkload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@org.springframework.cloud.openfeign.FeignClient("TRAINER-WORKLOAD-SERVICE")
public interface FeignClient {
    @PostMapping
    ResponseEntity<String> addTraining(@RequestHeader("Authorization") String bearerToken, TrainerWorkloadRequestData requestData);

    @DeleteMapping
    ResponseEntity<String> deleteTraining(@RequestHeader("Authorization") String bearerToken, TrainerWorkloadRequestData requestData);

    @GetMapping("/{username}")
    TrainerWorkload getTrainerWorkload(@RequestHeader("Authorization") String bearerToken, @PathVariable("username") String username);
}
