package com.kolya.gym.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;


public class PrometheusMetrics {

    public static Counter CreateTraineeCounter(MeterRegistry registry) {
        return Counter.builder("CreateTrainee.calls")
                .description("Indicates number of times CreateTrainee was called")
                .register(registry);
    }

    public static Counter CreateTrainerCounter(MeterRegistry registry) {
        return Counter.builder("CreateTrainer.calls")
                .description("Indicates number of times CreateTrainer was called")
                .register(registry);
    }
}
