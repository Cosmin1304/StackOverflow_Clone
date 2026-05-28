package com.utcn.notification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// Daca lipsesc credentialele Twilio, serviciul porneste totusi si sare peste trimitere (skip silentios).
@Service
public class WhatsAppSender {

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.whatsapp-from:}")
    private String fromWhatsApp;

    private boolean initialized = false;

    @PostConstruct
    void init() {
        if (notBlank(accountSid) && notBlank(authToken)) {
            Twilio.init(accountSid, authToken);
            initialized = true;
            System.out.println(">>> [notification-svc] Twilio initializat pentru WhatsApp.");
        } else {
            System.out.println(">>> [notification-svc] Twilio NU este configurat. Mesajele WhatsApp vor fi sarite.");
        }
    }

    @Async
    public void sendBanWhatsApp(String toPhone, String username) {
        if (!initialized) {
            System.err.println("[notification-svc] Twilio neconfigurat; nu trimit WhatsApp catre " + toPhone);
            return;
        }
        if (!notBlank(toPhone)) {
            System.err.println("[notification-svc] Fara numar de telefon; sar peste WhatsApp.");
            return;
        }
        try {
            String body = "Salut " + username + "! Contul tau de StackOverflow a fost banat de un moderator " +
                    "si nu te mai poti autentifica pana la ridicarea banului.";
            Message.creator(
                    new PhoneNumber("whatsapp:" + toPhone),
                    new PhoneNumber(fromWhatsApp),
                    body
            ).create();
            System.out.println(">>> [notification-svc] WhatsApp de ban trimis catre " + toPhone);
        } catch (Exception e) {
            System.err.println("[notification-svc] Eroare la trimiterea WhatsApp catre " + toPhone + ": " + e.getMessage());
        }
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
