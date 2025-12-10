# Panchmukhi Trading Brain Pro - Backend Dockerfile
# Multi-stage build for Java Spring Boot application

# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install dependencies for native libraries
RUN apk add --no-cache \
    curl \
    bash \
    gcompat \
    libstdc++ \
    && rm -rf /var/cache/apk/*

# Create non-root user
RUN addgroup -g 1000 panchmukhi && \
    adduser -D -s /bin/sh -u 1000 -G panchmukhi panchmukhi

# Copy built JAR from build stage
COPY --from=build --chown=panchmukhi:panchmukhi /app/target/panchmukhi-trading-backend-*.jar app.jar

# Create directories for logs and temp files
RUN mkdir -p /app/logs /app/temp && \
    chown -R panchmukhi:panchmukhi /app

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1

# Switch to non-root user
USER panchmukhi

# Expose port
EXPOSE 8083

# JVM optimization for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:MinRAMPercentage=25.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -XX:MaxGCPauseMillis=200 \
    -XX:+OptimizeStringConcat \
    -Djava.awt.headless=true \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=docker"

# Entry point
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Labels for better image management
LABEL maintainer="Panchmukhi Trading Brain Pro Team"
LABEL version="2.0.0"
LABEL description="AI-powered trading platform backend"
LABEL org.opencontainers.image.title="Panchmukhi Trading Backend"
LABEL org.opencontainers.image.description="Java Spring Boot backend for AI-powered trading platform"
LABEL org.opencontainers.image.version="2.0.0"