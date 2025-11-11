FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY microservice-viajes/pom.xml ./microservice-viajes/
COPY microservice-viajes/src ./microservice-viajes/src
WORKDIR /app/microservice-viajes
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/microservice-viajes/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]