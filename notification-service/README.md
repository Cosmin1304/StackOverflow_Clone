# notification-service

Microserviciu separat care trimite notificari (email + WhatsApp) cand un utilizator e banat.
Apelat de `spring_app` printr-un POST HTTP intern, asincron, fara sa blocheze tranzactia de ban.

## Cum se incadreaza

```
spring_app (8080)  --POST /api/notify/ban-->  notification-service (8081)
                                                    |
                                                    +--> Gmail SMTP (email)
                                                    +--> Twilio API (WhatsApp)
```

Daca microserviciul e picat sau credentialele lipsesc, ban-ul tot reuseste — esecul
notificarii e doar logat, nu propagat.

## Ce iti trebuie

- Docker + Docker Compose (rulam totul prin `docker compose`).
- Un cont Gmail cu 2-Step Verification activat (pentru a putea genera un App Password).
- Un cont Twilio gratuit (trial-ul vine cu credit suficient pentru testare).

Atat. Codul descarca Java 17 + Maven automat in container — nu trebuie sa le ai instalate local.

## Pasul 1: pregateste credentialele

### Gmail App Password
1. Activeaza 2-Step Verification pe contul Gmail: https://myaccount.google.com/security
2. Mergi la https://myaccount.google.com/apppasswords si creeaza un App Password
   (selecteaza "Mail" si numele aplicatiei). Vei primi un string de 16 caractere
   (fara spatii) — asta e parola pe care o folosesti, NU parola ta normala de Gmail.

### Twilio (WhatsApp Sandbox)
1. Fa cont pe https://www.twilio.com/try-twilio si confirma adresa de email + numarul de telefon.
2. Din Console Dashboard copiaza **Account SID** si **Auth Token** (sub "Account Info").
3. Mergi la **Messaging > Try it out > Send a WhatsApp message**. Twilio iti da un
   sandbox WhatsApp number (format: `whatsapp:+14155238886`) si o comanda
   `join <cuvant-cheie>`. Trimite acea comanda de pe telefonul tau prin WhatsApp catre
   numarul sandbox — asa numarul tau devine destinatar autorizat in trial.
4. Daca vrei sa primesti notificari pe alt telefon (ex: utilizatorul banat in test),
   numarul ala trebuie sa intre si el in sandbox prin acelasi mesaj `join`.

## Pasul 2: creeaza fisierul .env

In RADACINA proiectului (NU in `notification-service/`), creeaza un fisier numit `.env`
cu urmatorul continut, completand cu valorile tale:

```env
SPRING_MAIL_USERNAME=nume.tau@gmail.com
SPRING_MAIL_PASSWORD=xxxxxxxxxxxxxxxx
TWILIO_ACCOUNT_SID=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_AUTH_TOKEN=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
TWILIO_WHATSAPP_FROM=whatsapp:+14155238886
```

`SPRING_MAIL_PASSWORD` e App Password-ul de 16 caractere — fara spatii.
`TWILIO_WHATSAPP_FROM` e numarul sandbox Twilio (cu prefixul `whatsapp:`).

`.env` e in `.gitignore` — NU se urca pe GitHub. Fiecare coleg isi face propriul `.env`.

## Pasul 3: porneste tot stack-ul

Din radacina proiectului:

```bash
docker compose -f stack-overflow.yaml up -d --build
```

`--build` reconstruieste imaginile (necesar prima data si dupa modificari de cod).
Comanda porneste trei containere: `postgres`, `notification-service`, `spring_app`.

Verifica ca toate sunt sus:
```bash
docker compose -f stack-overflow.yaml ps
```

## Pasul 4: verifica ca microserviciul a pornit corect

```bash
docker compose -f stack-overflow.yaml logs notification-service
```

Cauta linia:
```
>>> [notification-svc] Twilio initializat pentru WhatsApp.
```

Daca apare `Twilio NU este configurat`, inseamna ca lipseste/e gresit `TWILIO_ACCOUNT_SID`
sau `TWILIO_AUTH_TOKEN` in `.env`.

## Pasul 5: testeaza fluxul end-to-end

1. Logheaza-te in aplicatie ca moderator (user `moderator`).
2. Mergi pe **Moderator Panel** (buton in navbar, vizibil doar pentru moderatori).
3. Apasa **Ban** la un utilizator care are emailul tau si telefonul tau in DB
   (sau modifica un user existent ca sa aiba aceste date).
4. Verifica:
   - **In consola** `spring_app`: `>>> Notificare ban trimisa catre notification-service`.
   - **In consola** `notification-service`: `>>> Email de ban trimis catre ...` si
     `>>> WhatsApp de ban trimis catre ...`.
   - **In Gmail-ul utilizatorului banat** (verifica si Spam): mesajul de ban.
   - **Pe WhatsApp-ul utilizatorului banat**: mesajul de ban de la numarul sandbox.

## Probleme frecvente

**`Authentication failed` in log la trimiterea email-ului**
- App Password gresit sau nu ai activat 2-Step Verification pe Gmail.
- `SPRING_MAIL_USERNAME` trebuie sa fie exact adresa contului care a generat App Password-ul.

**WhatsApp-ul nu ajunge**
- Destinatarul nu a trimis comanda `join <cuvant>` catre numarul sandbox. Trial-ul Twilio
  permite mesaje DOAR catre numere verificate in sandbox.
- Numarul de telefon din DB nu e in format E.164 (`+40712345678`, nu `0712345678`).

**`The character [_] is never valid in a domain name` in notification-service**
- Service-ul din `stack-overflow.yaml` se cheama `notification-service` (cu cratima).
  Tomcat 10 respinge underscore-uri in hostname. Nu redenumi service-ul.

**Email-ul nu ajunge la mine, desi am banat din panel**
- Mailul nu se trimite catre tine (moderatorul), ci catre adresa de email a utilizatorului
  BANAT. Verifica adresa din DB.

## Rulare standalone (fara Docker, optional)

Daca vrei sa rulezi doar microserviciul direct cu Maven:

```bash
cd notification-service
SPRING_MAIL_USERNAME=... SPRING_MAIL_PASSWORD=... TWILIO_ACCOUNT_SID=... TWILIO_AUTH_TOKEN=... \
TWILIO_WHATSAPP_FROM=whatsapp:+14155238886 \
mvn spring-boot:run
```

Asculta pe `localhost:8081`. Trebuie sa setezi `NOTIFICATION_SERVICE_URL=http://localhost:8081`
ca `spring_app` sa il gaseasca.

## Oprire

```bash
docker compose -f stack-overflow.yaml down
```

Sau doar microserviciul:
```bash
docker compose -f stack-overflow.yaml stop notification-service
```
