FROM openjdk:21.0.2-jdk-slim

ARG LIQUIBASE_VERSION=4.25.1

WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/MaslyakBank_Token-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
