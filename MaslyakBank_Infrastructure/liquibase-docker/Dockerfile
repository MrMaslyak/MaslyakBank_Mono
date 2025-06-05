FROM debian:bullseye

# Установка curl, unzip, PostgreSQL client, Java
RUN apt-get update && \
    apt-get install -y curl unzip postgresql-client openjdk-17-jdk && \
    apt-get clean

# Скачиваем и устанавливаем Liquibase
RUN curl -Lo liquibase.zip https://github.com/liquibase/liquibase/releases/download/v4.25.1/liquibase-4.25.1.zip && \
    unzip liquibase.zip -d /opt/liquibase && \
    ln -s /opt/liquibase/liquibase /usr/local/bin/liquibase && \
    chmod +x /opt/liquibase/liquibase && \
    rm liquibase.zip

# Установка переменной окружения JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Рабочая директория
WORKDIR /liquibase

# Копируем миграции
COPY ./liquibase-docker /liquibase
