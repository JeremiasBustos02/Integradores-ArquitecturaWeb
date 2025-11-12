# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY microservice-admin/pom.xml ./microservice-admin/
COPY microservice-admin/src ./microservice-admin/src
WORKDIR /app/microservice-admin
RUN mvn -q -e -B clean package -DskipTests
# Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/microservice-admin/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","app.jar"]
