FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app
# Copy the compiled JAR file into the container

EXPOSE 8080

COPY target/ToDoAPI-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]