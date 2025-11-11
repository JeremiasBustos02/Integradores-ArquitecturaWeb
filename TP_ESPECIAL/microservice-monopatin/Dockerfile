FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY microservice-monopatin/pom.xml ./microservice-monopatin/
COPY microservice-monopatin/src ./microservice-monopatin/src
WORKDIR /app/microservice-monopatin
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/microservice-monopatin/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]