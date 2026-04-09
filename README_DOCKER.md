# 🐳 Ghid Docker Compose - Pornirea Aplicației JavaSpringCRUD

Acest document te va ghida pas cu pas pentru a porni aplicația folosind Docker Compose.

---

## 📋 Cerințe Preliminare

Înainte de a porni aplicația, asigură-te că ai instalat:

### 1. **Docker Desktop**
- **Windows/Mac:** Descarcă din https://www.docker.com/products/docker-desktop
- **Linux:** 
  ```bash
  sudo apt-get update
  sudo apt-get install docker.io docker-compose
  ```

### 2. **Verifică Instalarea**
Deschide terminalul și rulează:
```bash
docker --version
docker compose --version
```

Ar trebui să vezi versiuni asemănătoare cu:
```
Docker version 27.5.1, build 9f9e405
Docker Compose version v2.x.x
```

---

## 🚀 Pași pentru Pornirea Aplicației

### **Pasul 1: Navigează la Directorul Proiectului**

```bash
cd calea/catre/JavaSpringCRUD_Demo
```

Exemplu pe Mac/Linux:
```bash
cd ~/Projects/JavaSpringCRUD_Demo
```

Exemplu pe Windows (PowerShell):
```powershell
cd C:\Users\NumeleUser\Projects\JavaSpringCRUD_Demo
```

---

### **Pasul 2: Setează Variabilele de Mediu**

Înainte de a porni containerele, trebuie să setezi variabilele de autentificare pentru PostgreSQL.

#### 🔧 **Pe Mac/Linux:**

```bash
export POSTGRES_PASSWORD=ADMIN
export POSTGRES_USER=ADMIN
export POSTGRES_DB=stackoverflow
```

#### 🔧 **Pe Windows (Command Prompt):**

```cmd
set POSTGRES_PASSWORD=ADMIN
set POSTGRES_USER=ADMIN
set POSTGRES_DB=stackoverflow
```

#### 🔧 **Pe Windows (PowerShell):**

```powershell
$env:POSTGRES_PASSWORD="ADMIN"
$env:POSTGRES_USER="ADMIN"
$env:POSTGRES_DB="stackoverflow"
```

---

### **Pasul 3: Pornește Aplicația cu Docker Compose**

```bash
docker compose -f stack-overflow.yaml up --build
```

**Ce se întâmplă:**
- `docker compose` - instrucțiunea Docker Compose
- `-f stack-overflow.yaml` - specifică fișierul de configurare
- `up` - pornește containerele
- `--build` - reconstruiește imaginile dacă sunt schimbări în cod

---

### **Pasul 4: Așteptă Mesajul de Succes**

Caută în terminal mesajele acestea:

```
✔ postgres Pulled                    7.9s 
✔ Network javaspringcrud_demo_default Created
✔ Container javaspringcrud_demo-postgres-1 Created
✔ Container javaspringcrud_demo-spring_app-1 Created
```

Și ceva de genul:

```
spring_app-1  | Tomcat started on port(s): 8080 (http) with context path ''
spring_app-1  | Started DemoApplication in X.XXX seconds
```

🎉 **Bravo! Aplicația rulează!**

---

## 🌐 Accesează Aplicația

Deschide browserul și mergi la:

```
http://localhost:8080
```

---

## 📊 Verifică Statusul Containerelor

Intr-un alt terminal (pe același calculator), poți verifica că containerele rulează:

```bash
docker ps
```

Ar trebui să vezi ceva similar:

```
CONTAINER ID   IMAGE                              COMMAND                  STATUS
abc123def456   javaspringcrud_demo-spring_app     "java -jar app.jar"     Up 5 minutes
xyz789uvw012   postgres:15-alpine                 "docker-entrypoint.s"   Up 5 minutes
```

---

## 🔗 Conexiune la Baza de Date

Dacă vrei să te conectezi direct la PostgreSQL (de ex. cu DBeaver, pgAdmin, etc.):

| Parametru | Valoare |
|-----------|---------|
| **Host** | `localhost` |
| **Port** | `5432` |
| **Username** | `postgres` |
| **Password** | `ADMIN` |
| **Database** | `stackoverflow` |

---

## 🛑 Oprirea Aplicației

Pentru a opri containerele, apasă:

```
Ctrl + C
```

În terminal, sau rulează:

```bash
docker compose -f stack-overflow.yaml down
```

---

## 🧹 Ștergerea Completă (Reset)

Dacă vrei să ștergi totul și să pornești complet de zero:

```bash
# Oprește containerele
docker compose -f stack-overflow.yaml down

# Șterge volumul persistent (baza de date)
docker volume rm javaspringcrud_demo_postgres_data

# Pornește din nou cu pași 1-4
```

⚠️ **ATENȚIE:** Aceasta va șterge toată baza de date!

---

## 🐛 Troubleshooting

### **Problema 1: "Docker daemon is not running"**

**Soluție:**
- **Windows/Mac:** Deschide Docker Desktop din Applications
- **Linux:** Rulează `sudo systemctl start docker`

---

### **Problema 2: "Port 8080 already in use"**

Altă aplicație folosește portul 8080.

**Soluție:**
```bash
# Găsește ce folosește portul 8080
lsof -i :8080  # Mac/Linux
netstat -ano | findstr :8080  # Windows

# Oprește aplicația sau schimbă portul în docker-compose
```

---

### **Problema 3: "Connection to database failed"**

**Soluție:**
1. Asigură-te că ai setat variabilele de mediu corect (Pasul 2)
2. Șterge volumul persistent și pornește din nou:
   ```bash
   docker volume rm javaspringcrud_demo_postgres_data
   docker compose -f stack-overflow.yaml up --build
   ```

---

### **Problema 4: "Build failed - Maven errors"**

Asigură-te că `pom.xml` și `src/` sunt în directorul proiectului.

**Soluție:**
```bash
# Curăță și reconstruiește
docker compose -f stack-overflow.yaml down
docker system prune -a
docker compose -f stack-overflow.yaml up --build
```

---

## 📝 Fișiere Importante

```
JavaSpringCRUD_Demo/
├── stack-overflow.yaml       ← Configurarea Docker Compose
├── Dockerfile               ← Instrucțiuni pentru build-ul aplicației
├── pom.xml                  ← Dependențele Maven
├── src/                     ← Codul sursă Java
├── target/                  ← Fișierele compilate
└── README_DOCKER.md        ← Acest fișier
```

---

## 🎓 Comenzi Utile Docker Compose

```bash
# Pornire containerelor în background
docker compose -f stack-overflow.yaml up -d

# Vezi logurile aplicației
docker compose -f stack-overflow.yaml logs -f spring_app

# Vezi logurile PostgreSQL
docker compose -f stack-overflow.yaml logs -f postgres

# Execută comande în container
docker compose -f stack-overflow.yaml exec spring_app bash

# Reconstruiește numai o anumită imagine
docker compose -f stack-overflow.yaml up --build spring_app
```

---

## 🔐 Variabile de Mediu Explicație

| Variabilă | Valoare | Explicație |
|-----------|---------|-----------|
| `POSTGRES_PASSWORD` | `ADMIN` | Parola pentru user-ul postgres |
| `POSTGRES_USER` | `postgres` | User-ul principal PostgreSQL |
| `POSTGRES_DB` | `stackoverflow` | Numele bazei de date create automat |

⚠️ **IMPORTANT:** Pe producție, nu folosi parolă simplu "ADMIN"! Folosește parolă tare și variabile de mediu securizate.

---

## 📖 Structura Docker Compose

Fișierul `stack-overflow.yaml` conține 2 servicii:

### **1. PostgreSQL (Baza de Date)**
- Imagine: `postgres:15-alpine`
- Port: `5432` (intern/extern)
- Volum persistent: `postgres_data` (datele sunt salvate chiar dacă containerul se șterge)

### **2. Spring Boot Application**
- Imagine: Construită din `Dockerfile`
- Port: `8080` (intern/extern)
- Depinde de: PostgreSQL
- Limbaj: Java 17

---

## ✅ Checklist de Pornire Rapidă

- [ ] Docker Desktop instalat și rulând
- [ ] Am navigat la directorul corect: `JavaSpringCRUD_Demo`
- [ ] Am setat variabilele de mediu (POSTGRES_PASSWORD, POSTGRES_USER, POSTGRES_DB)
- [ ] Am rulat: `docker compose -f stack-overflow.yaml up --build`
- [ ] Vad mesajul: "Tomcat started on port(s): 8080"
- [ ] Pot accesa: `http://localhost:8080`

---

## 📞 Support

Dacă întâmpini probleme:

1. **Verifică logurile:**
   ```bash
   docker compose -f stack-overflow.yaml logs
   ```

2. **Contactează team lead-ul** cu mesajul de eroare exact

3. **Șterge și reconstruiește:**
   ```bash
   docker volume prune
   docker compose -f stack-overflow.yaml down
   docker compose -f stack-overflow.yaml up --build
   ```

---

## 🎉 Gata!

Aplicația ar trebui să ruleze acum! Poți:
- 🌐 Accesa interfața web la `http://localhost:8080`
- 💾 Conecta tool-uri externe la baza de date (localhost:5432)
- 📝 Modifica codul și vedea schimbările (repornire necesară)

**Happy coding! 🚀**
