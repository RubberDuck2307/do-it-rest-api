FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8080

COPY target/ToDoAPI-1.0.0.jar app.jar

CMD ["java", "-jar", "app.jar"]

