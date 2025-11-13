FROM openjdk:21-ea-slim



WORKDIR /app

COPY target/MaslyakBank_Core-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
