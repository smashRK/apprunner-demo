# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN echo "Building application..." && \
    mvn clean package -DskipTests && \
    echo "Build complete. Contents of target directory:" && \
    ls -la target/

# Runtime stage
FROM openjdk:17-slim
WORKDIR /app

# Add debugging tools
RUN apt-get update && \
    apt-get install -y curl procps && \
    rm -rf /var/lib/apt/lists/*

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Create directory for logs
RUN mkdir -p /app/logs && \
    chmod 777 /app/logs

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV LOGGING_FILE_PATH="/app/logs/spring-boot-app.log"
ENV LOGGING_LEVEL_ROOT=DEBUG
ENV LOGGING_LEVEL_ORG_SPRINGFRAMEWORK=DEBUG
ENV SPRING_JPA_SHOW_SQL=true
ENV SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start application with logging configuration
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} \
    -Dlogging.file.path=${LOGGING_FILE_PATH} \
    -Dlogging.level.root=${LOGGING_LEVEL_ROOT} \
    -Dlogging.level.org.springframework=${LOGGING_LEVEL_ORG_SPRINGFRAMEWORK} \
    -Dspring.jpa.show-sql=${SPRING_JPA_SHOW_SQL} \
    -Dspring.jpa.properties.hibernate.format_sql=${SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL} \
    -jar app.jar"]
