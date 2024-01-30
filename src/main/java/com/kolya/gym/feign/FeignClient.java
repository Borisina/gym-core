package com.kolya.gym.feign;

import com.kolya.gym.domain.TrainerWorkload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@org.springframework.cloud.openfeign.FeignClient("TRAINER-WORKLOAD-SERVICE")
public interface FeignClient {
    @GetMapping("/trainer-workload/{username}")
    TrainerWorkload getTrainerWorkload(@RequestHeader("Authorization") String bearerToken, @PathVariable("username") String username);
}
