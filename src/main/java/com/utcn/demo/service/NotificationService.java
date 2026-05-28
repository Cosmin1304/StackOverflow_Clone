package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// @Async: nu blocam tranzactia de ban daca microserviciul e lent/picat (esuarea apelului este logata, nu propagata).
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Async
    public void notifyUserBanned(User user) {
        if (user == null || user.getUsername() == null) return;

        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        String username = user.getUsername();

        Map<String, String> body = new HashMap<>();
        body.put("email", email == null ? "" : email);
        body.put("phoneNumber", phoneNumber == null ? "" : phoneNumber);
        body.put("username", username);

        try {
            restTemplate.postForEntity(notificationServiceUrl + "/api/notify/ban", body, Void.class);
            System.out.println(">>> Notificare ban trimisa catre notification-service pentru " + username);
        } catch (Exception e) {
            System.err.println("Eroare la apelul notification-service: " + e.getMessage());
        }
    }
}
