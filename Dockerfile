FROM openjdk:17-jdk-slim-buster
MAINTAINER teste
ENTRYPOINT ["java","-jar","/message-server-1.0.0.jar"]