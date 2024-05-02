FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY ./pom.xml /app
COPY ./src /app/src

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/weather-api-*.jar /app/weather-api.jar

ENTRYPOINT ["java", "-jar", "/app/weather-api.jar"]