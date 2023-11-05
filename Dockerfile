# First stage: build the application
FROM maven:3.8.3-jdk-11 AS build
COPY . /app
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN sudo ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]