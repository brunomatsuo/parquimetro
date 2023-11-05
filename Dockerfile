# First stage: build the application
FROM maven:3.8.3-jdk-11 AS build
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests

# Second stage: create a slim image
FROM openjdk:17-jdk-slim-buster
COPY --from=build /app/target/parquimetro-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]