# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set Liquibase version
ENV LIQUIBASE_VERSION=4.25.1

# Set working directory
WORKDIR /opt/liquibase

# Install curl, unzip, and dependencies
RUN apt-get update && apt-get install -y curl unzip && \
    curl -L -o liquibase.tar.gz https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.tar.gz && \
    tar -xvzf liquibase.tar.gz --strip-components=1 && \
    rm liquibase.tar.gz && \
    ln -s /opt/liquibase/liquibase /usr/local/bin/liquibase && \
    chmod +x /opt/liquibase/liquibase && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Optionally copy your changelogs and JDBC drivers
# COPY ./changelog /liquibase/changelog
# COPY ./drivers/postgresql.jar /opt/liquibase/lib/

# Default command (can be overridden)
ENTRYPOINT ["liquibase"]
