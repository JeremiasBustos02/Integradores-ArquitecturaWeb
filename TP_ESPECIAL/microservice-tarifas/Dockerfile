FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY microservice-tarifas/pom.xml ./microservice-tarifas/
COPY microservice-tarifas/src ./microservice-tarifas/src
WORKDIR /app/microservice-tarifas
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/microservice-tarifas/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
