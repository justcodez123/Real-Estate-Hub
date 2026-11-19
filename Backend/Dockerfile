# Stage 1: Build Spring Boot executable JAR with Maven & JDK 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache Maven dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build production JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal JRE 17 production runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Run as non-root user for container security
RUN useradd -m appuser
USER appuser

COPY --from=build /app/target/real-estate-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
