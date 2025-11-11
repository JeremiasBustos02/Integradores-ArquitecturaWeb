FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]