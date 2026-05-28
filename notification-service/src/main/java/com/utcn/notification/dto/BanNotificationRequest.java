package com.utcn.notification.dto;

// Payload-ul JSON primit de la spring_app la banarea unui utilizator.
public record BanNotificationRequest(
        String email,
        String phoneNumber,
        String username
) {}
