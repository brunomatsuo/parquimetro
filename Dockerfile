FROM openjdk:17-jdk-slim-buster
MAINTAINER teste
COPY target/docker-parquimetro-0.0.1-SNAPSHOT.jar parquimetro-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/message-server-1.0.0.jar"]