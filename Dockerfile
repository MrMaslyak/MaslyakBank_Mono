FROM openjdk:24-ea-21-jdk-slim

WORKDIR /app

# Копируем JAR-файл в контейнер
COPY ./out/artifacts/MaslyakBank_Token_jar/MaslyakBank_Token.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
