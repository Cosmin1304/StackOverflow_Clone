package com.utcn.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

// Aplicatie Spring Boot separata — microserviciul de notificari.
// Asculta pe 8081 si expune POST /api/notify/ban pentru clienti interni (spring_app).
@SpringBootApplication
@EnableAsync
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
