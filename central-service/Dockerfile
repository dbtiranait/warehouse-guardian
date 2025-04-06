# Use a lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Create app directory
WORKDIR /app

# Copy the built JAR into the container
COPY target/central-service-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
