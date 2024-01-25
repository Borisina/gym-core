package com.kolya.gym.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PrometheusMetrics {

    private final Counter createTraineeCounter;
    private final Counter createTrainerCounter;
    private final MeterRegistry registry;

    @Autowired
    public PrometheusMetrics(MeterRegistry registry) {
        this.registry = registry;
        createTraineeCounter = CreateTraineeCounter();
        createTrainerCounter = CreateTrainerCounter();
    }

    private Counter CreateTraineeCounter() {
        return Counter.builder("CreateTrainee.calls")
                .description("Indicates number of times CreateTrainee was called")
                .register(registry);
    }

    private Counter CreateTrainerCounter() {
        return Counter.builder("CreateTrainer.calls")
                .description("Indicates number of times CreateTrainer was called")
                .register(registry);
    }

    public void incrementCreateTraineeCounter(){
        createTraineeCounter.increment();
    }

    public void incrementCreateTrainerCounter(){
        createTrainerCounter.increment();
    }
}
