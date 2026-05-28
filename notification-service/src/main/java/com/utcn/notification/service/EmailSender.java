package com.utcn.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// Trimite email-ul de notificare. @Async ca sa raspundem 202 rapid catre apelant
// si sa nu blocam tranzactia de ban din spring_app daca SMTP-ul e lent.
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Async
    public void sendBanEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject("Contul tau StackOverflow a fost suspendat");
            message.setText(
                    "Salut " + username + ",\n\n" +
                    "Contul tau de pe StackOverflow a fost banat de un moderator. " +
                    "Nu te vei mai putea autentifica pana cand banul este ridicat.\n\n" +
                    "— Echipa StackOverflow"
            );
            mailSender.send(message);
            System.out.println(">>> [notification-svc] Email de ban trimis catre " + to);
        } catch (Exception e) {
            System.err.println("[notification-svc] Eroare la trimiterea email-ului catre " + to + ": " + e.getMessage());
        }
    }
}
