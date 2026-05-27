package com.utcn.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// Trimite mesaje WhatsApp prin Twilio. Trimiterea e @Async (fire-and-forget),
// iar dacă lipsesc credentialele Twilio serviciul doar loghează și sare peste.
@Service
public class WhatsAppService {

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    // Numărul sandbox Twilio, în format "whatsapp:+14155238886".
    @Value("${twilio.whatsapp-from:}")
    private String fromWhatsApp;

    private boolean initialized = false;

    @PostConstruct
    void init() {
        if (notBlank(accountSid) && notBlank(authToken)) {
            Twilio.init(accountSid, authToken);
            initialized = true;
            System.out.println(">>> Twilio inițializat pentru notificări WhatsApp.");
        } else {
            System.out.println(">>> Twilio NU este configurat (lipsesc SID/token). Mesajele WhatsApp vor fi sărite.");
        }
    }

    @Async
    public void sendBanWhatsApp(String toPhone, String username) {
        if (!initialized) {
            System.err.println("Twilio neconfigurat; nu trimit WhatsApp către " + toPhone);
            return;
        }
        if (!notBlank(toPhone)) {
            System.err.println("Utilizatorul nu are număr de telefon; sar peste notificarea WhatsApp.");
            return;
        }
        try {
            String body = "Salut " + username + "! Contul tău de StackOverflow a fost banat de un moderator " +
                    "și nu te mai poți autentifica până la ridicarea banului.";
            Message.creator(
                    new PhoneNumber("whatsapp:" + toPhone), // destinatar
                    new PhoneNumber(fromWhatsApp),           // expeditor (numărul sandbox)
                    body
            ).create();
            System.out.println(">>> WhatsApp de ban trimis către " + toPhone);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea WhatsApp către " + toPhone + ": " + e.getMessage());
        }
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
