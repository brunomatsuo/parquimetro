FROM openjdk:17-jdk-slim-buster
EXPOSE 8080
ARG JAR_FILE=target/demo-app-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","parquimetro-0.0.1-SNAPSHOT.jar"]