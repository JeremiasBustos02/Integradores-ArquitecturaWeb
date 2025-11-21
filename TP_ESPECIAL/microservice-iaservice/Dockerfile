FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY microservice-iaservice/pom.xml ./microservice-iaservice/

WORKDIR /app/microservice-iaservice
RUN mvn dependency:go-offline -B

COPY microservice-iaservice/src ./src

RUN mvn clean compile -DskipTests -X
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/microservice-iaservice/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]