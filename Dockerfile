# Use Eclipse Temurin OpenJDK 17 slim image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Maven-built jar
COPY target/Online-food-ordering-app-0.0.1-SNAPSHOT.jar app.jar

# Cloud Run expects the app to listen on $PORT
ENV PORT 8080
ENV JAVA_OPTS="-Dserver.port=$PORT"

# Expose the port (good practice)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
