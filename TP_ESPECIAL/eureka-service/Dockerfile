FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY eureka-service/pom.xml ./eureka-service/
COPY eureka-service/src ./eureka-service/src
WORKDIR /app/eureka-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache wget
WORKDIR /app
COPY --from=build /app/eureka-service/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
