FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY microservice-facturacion/pom.xml ./microservice-facturacion/
COPY microservice-facturacion/src ./microservice-facturacion/src
WORKDIR /app/microservice-facturacion
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/microservice-facturacion/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
