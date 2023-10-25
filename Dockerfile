FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8080

COPY target/ToDoAPI-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]