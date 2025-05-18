FROM eclipse-temurin:17-jdk-alpine
WORKDIR /config-service
COPY target/*.jar config-service.jar
ENTRYPOINT ["java", "-jar", "config-service.jar"]