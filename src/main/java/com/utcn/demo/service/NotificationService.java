package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Punct unic de fan-out pentru notificări. Rulează în thread-ul apelant (în tranzacție),
// citește câmpurile simple ale utilizatorului și deleagă trimiterea efectivă către
// senderele @Async (email + WhatsApp), care rulează pe thread-uri separate.
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    public void notifyUserBanned(User user) {
        if (user == null) return;

        String username = user.getUsername();

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            emailService.sendBanEmail(user.getEmail(), username);
        }
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isBlank()) {
            whatsAppService.sendBanWhatsApp(user.getPhoneNumber(), username);
        }
    }
}
