package com.utcn.notification.controller;

import com.utcn.notification.dto.BanNotificationRequest;
import com.utcn.notification.service.EmailSender;
import com.utcn.notification.service.WhatsAppSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailSender emailSender;
    private final WhatsAppSender whatsAppSender;

    @PostMapping("/ban")
    public ResponseEntity<Void> notifyBan(@RequestBody BanNotificationRequest req) {
        if (req == null || req.username() == null || req.username().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (req.email() != null && !req.email().isBlank()) {
            emailSender.sendBanEmail(req.email(), req.username());
        }
        if (req.phoneNumber() != null && !req.phoneNumber().isBlank()) {
            whatsAppSender.sendBanWhatsApp(req.phoneNumber(), req.username());
        }
        return ResponseEntity.accepted().build();
    }
}
