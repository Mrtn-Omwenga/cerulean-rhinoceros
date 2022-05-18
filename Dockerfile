# Build image
FROM openjdk:17-slim-buster as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# source code is likely to change more often than dependencies.
RUN ./mvnw -B dependency:go-offline
COPY src src
RUN ./mvnw -B package

# Run time image
FROM openjdk:17-slim-buster
COPY --from=build target/donations-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "donations-0.0.1-SNAPSHOT.jar"]