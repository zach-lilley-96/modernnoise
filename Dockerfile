# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy maven executable and pom
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# The JAR name comes from pom.xml: <artifactId>-<version>.jar
COPY --from=build /app/target/modernnoise-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Render sets a PORT env var automatically. 
# Spring Boot picks up SERVER_PORT if we pass it or set it.
ENTRYPOINT ["java", "-jar", "app.jar"]
