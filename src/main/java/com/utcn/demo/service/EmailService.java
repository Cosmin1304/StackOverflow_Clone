package com.utcn.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// Trimite email-uri prin SMTP (Gmail). Metoda de trimitere e @Async ca să nu
// blocheze acțiunea de ban dacă serverul SMTP e lent sau pică.
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Async
    public void sendBanEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject("Contul tău StackOverflow a fost suspendat");
            message.setText(
                    "Salut " + username + ",\n\n" +
                    "Contul tău de pe StackOverflow a fost banat de un moderator. " +
                    "Nu te vei mai putea autentifica până când banul este ridicat.\n\n" +
                    "Dacă crezi că este o greșeală, te rugăm să contactezi echipa de moderare.\n\n" +
                    "— Echipa StackOverflow"
            );
            mailSender.send(message);
            System.out.println(">>> Email de ban trimis către " + to);
        } catch (Exception e) {
            // Nu propagăm eroarea: o notificare eșuată nu trebuie să strice banul.
            System.err.println("Eroare la trimiterea email-ului de ban către " + to + ": " + e.getMessage());
        }
    }
}
