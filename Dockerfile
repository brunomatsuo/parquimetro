FROM maven:3.6.3-openjdk-14-slim AS build
WORKDIR /build
# copy just pom.xml (dependencies and dowload them all for offline access later - cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B
# copy source files and compile them (.dockerignore should handle what to copy)
COPY . .
RUN mvn package
# Explode fat runnable JARS
ARG DEPENDENCY=/build/target/dependency
RUN mkdir -p ${DEPENDENCY} && (cd ${DEPENDENCY}; jar -xf ../*.jar)
# Runnable image
FROM openjdk:17-jdk-slim-buster as runnable
VOLUME /tmp
VOLUME /logs
ARG DEPENDENCY=/build/target/dependency
# Create User&Group to not run docker images with root user
RUN addgroup -S awesome && adduser -S awesome -G awesome
USER awesome
# Copy libraries & meta-info & classes
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# Run application
ENTRYPOINT ["java","-cp","app:app/lib/*","com.myawesomeness.Application"]