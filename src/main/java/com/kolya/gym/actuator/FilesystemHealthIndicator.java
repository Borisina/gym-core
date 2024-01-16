package com.kolya.gym.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FilesystemHealthIndicator implements HealthIndicator {

    private static final File ROOT = new File("/");

    @Override
    public Health health() {
        long freeSpace = ROOT.getFreeSpace();
        long totalSpace = ROOT.getTotalSpace();
        double freePercentage = ((double)freeSpace / totalSpace) * 100;

        Health.Builder builder;
        if (freePercentage < 10.0) {
            builder = Health.down();
        } else {
            builder = Health.up();
        }

        return builder.withDetail("freeSpace", freeSpace)
                .withDetail("totalSpace", totalSpace)
                .withDetail("freePercentage", freePercentage)
                .build();
    }
}