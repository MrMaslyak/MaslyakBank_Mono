FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/MaslyakBank_Core-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
