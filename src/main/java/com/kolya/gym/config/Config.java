package com.kolya.gym.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.kolya.gym")
@PropertySource("classpath:application.properties")
public class Config {
    @Bean
    public Logger logger(){
        Logger logger = LogManager.getLogger(Config.class);
        return logger;
    }
}
