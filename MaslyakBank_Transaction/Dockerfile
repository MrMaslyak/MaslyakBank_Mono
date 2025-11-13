FROM openjdk:21-ea-slim

WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/MaslyakBank_Transaction-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
