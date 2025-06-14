FROM openjdk:17-slim

ENV LIQUIBASE_VERSION=4.32.0

# Установим зависимости
RUN apt-get update && \
    apt-get install -y curl unzip bash && \
    rm -rf /var/lib/apt/lists/*

# Установка Liquibase
RUN curl -L -o /tmp/liquibase.zip https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.zip && \
    unzip /tmp/liquibase.zip -d /opt/liquibase && \
    rm /tmp/liquibase.zip && \
    chmod +x /opt/liquibase/liquibase

ENV PATH="/opt/liquibase:${PATH}"

# Рабочая директория
WORKDIR /liquibase

# Просто оставим его "живым"
CMD ["tail", "-f", "/dev/null"]
