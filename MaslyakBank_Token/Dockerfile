FROM openjdk:21-ea-slim

WORKDIR /app

# Копируем JAR-файл в контейнер
COPY MaslyakBank_Token/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
