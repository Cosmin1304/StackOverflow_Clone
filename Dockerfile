FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
# Pasul 2: Run (folosim doar JRE-ul, mult mai ușor)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiem fișierul .jar compilat din primul stagiu (build)
# Verifică dacă numele fișierului tău în pom.xml generează "demo.jar" sau schimbă numele mai jos
COPY --from=build /app/target/*.jar app.jar

# Expunem portul aplicației
EXPOSE 8080

# Pornim aplicația
ENTRYPOINT ["java", "-jar", "app.jar"]