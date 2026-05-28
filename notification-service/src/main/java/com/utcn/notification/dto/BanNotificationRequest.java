package com.utcn.notification.dto;

public record BanNotificationRequest(
        String email,
        String phoneNumber,
        String username
) {}
