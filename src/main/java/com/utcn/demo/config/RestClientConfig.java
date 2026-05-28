package com.utcn.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Spring Boot nu ofera RestTemplate ca bean auto-configurat — il declaram aici.
// Folosit de NotificationService pentru a apela microserviciul notification-service.
@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
