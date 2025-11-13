FROM openjdk:21-ea-slim-bookworm

WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
