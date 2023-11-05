FROM openjdk:17-jdk-slim-buster
MAINTAINER teste
ENTRYPOINT ["java","-jar","/parquimetro-0.0.1-SNAPSHOT.jar"]