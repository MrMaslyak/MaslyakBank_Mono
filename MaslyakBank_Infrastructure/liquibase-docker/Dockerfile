FROM openjdk:21-slim

ARG LIQUIBASE_VERSION=4.25.1
ARG SPRING_BOOT_VERSION=3.4.5
ARG HIBERNATE_VERSION=6.4.4.Final

# Установка зависимостей
RUN apt-get update && \
    apt-get install -y curl unzip bash && \
    rm -rf /var/lib/apt/lists/*

# Установка Liquibase
RUN curl -L -o /tmp/liquibase.zip https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.zip && \
    unzip /tmp/liquibase.zip -d /opt/liquibase && \
    rm /tmp/liquibase.zip && \
    ln -s /opt/liquibase/liquibase /usr/local/bin/liquibase

# Создание папки lib для зависимостей
RUN mkdir -p /opt/liquibase/lib

# Spring Framework 6.1.16 используется в Spring Boot 3.4.5
RUN curl --fail -L -o /opt/liquibase/lib/spring-core-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-core/6.1.16/spring-core-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-context-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-context/6.1.16/spring-context-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-beans-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-beans/6.1.16/spring-beans-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-expression-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-expression/6.1.16/spring-expression-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-aop-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-aop/6.1.16/spring-aop-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-tx-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-tx/6.1.16/spring-tx-6.1.16.jar

# Spring Boot зависимости (нужны для автоконфигурации)
RUN curl --fail -L -o /opt/liquibase/lib/spring-boot-3.4.5.jar https://repo1.maven.org/maven2/org/springframework/boot/spring-boot/3.4.5/spring-boot-3.4.5.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-boot-autoconfigure-3.4.5.jar https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-autoconfigure/3.4.5/spring-boot-autoconfigure-3.4.5.jar

# Hibernate зависимости (точная версия из pom)
RUN curl --fail -L -o /opt/liquibase/lib/hibernate-core-${HIBERNATE_VERSION}.jar https://repo1.maven.org/maven2/org/hibernate/orm/hibernate-core/${HIBERNATE_VERSION}/hibernate-core-${HIBERNATE_VERSION}.jar

# Jakarta зависимости (для Spring Boot 3.x нужны Jakarta, не javax)
RUN curl --fail -L -o /opt/liquibase/lib/jakarta.persistence-api-3.1.0.jar https://repo1.maven.org/maven2/jakarta/persistence/jakarta.persistence-api/3.1.0/jakarta.persistence-api-3.1.0.jar
RUN curl --fail -L -o /opt/liquibase/lib/jakarta.annotation-api-2.1.1.jar https://repo1.maven.org/maven2/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar
RUN curl --fail -L -o /opt/liquibase/lib/jakarta.transaction-api-2.0.1.jar https://repo1.maven.org/maven2/jakarta/transaction/jakarta.transaction-api/2.0.1/jakarta.transaction-api-2.0.1.jar

# Дополнительные зависимости Hibernate
RUN curl --fail -L -o /opt/liquibase/lib/antlr4-runtime-4.13.0.jar https://repo1.maven.org/maven2/org/antlr/antlr4-runtime/4.13.0/antlr4-runtime-4.13.0.jar
RUN curl --fail -L -o /opt/liquibase/lib/byte-buddy-1.15.11.jar https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy/1.15.11/byte-buddy-1.15.11.jar
RUN curl --fail -L -o /opt/liquibase/lib/jboss-logging-3.6.1.Final.jar https://repo1.maven.org/maven2/org/jboss/logging/jboss-logging/3.6.1.Final/jboss-logging-3.6.1.Final.jar

# Классы для работы с аннотациями
RUN curl --fail -L -o /opt/liquibase/lib/classmate-1.7.0.jar https://repo1.maven.org/maven2/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar

# Liquibase Hibernate6 плагин (точная версия из pom)
RUN curl --fail -L -o /opt/liquibase/lib/liquibase-hibernate6-4.25.0.jar https://github.com/liquibase/liquibase-hibernate/releases/download/v4.25.0/liquibase-hibernate6-4.25.0.jar

# PostgreSQL драйвер (версия из pom)
RUN curl --fail -L -o /opt/liquibase/lib/postgresql-42.7.2.jar https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.2/postgresql-42.7.2.jar
RUN curl --fail -L -o /opt/liquibase/lib/ч  objenesis-3.4.jar https://repo1.maven.org/maven2/org/objenesis/objenesis/3.4/objenesis-3.4.jar


RUN curl --fail -L -o /opt/liquibase/lib/tx-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-tx/6.1.16/spring-tx-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/jdbc-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-jdbc/6.1.16/spring-jdbc-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/spring-orm-6.1.16.jar https://repo1.maven.org/maven2/org/springframework/spring-orm/6.1.16/spring-orm-6.1.16.jar
RUN curl --fail -L -o /opt/liquibase/lib/commons-logging-1.2.jar https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar

# Копирование jar с entity-классами
COPY target/libs/MaslyakBank_Core-0.0.1-SNAPSHOT.jar /opt/liquibase/lib/

# Установка прав доступа
RUN chmod +x /opt/liquibase/liquibase

WORKDIR /liquibase-docker

# Переменные окружения для Java 21
ENV JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED"

CMD ["tail", "-f", "/dev/null"]