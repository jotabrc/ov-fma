FROM eclipse-temurin:17-jdk-alpine
WORKDIR /auth-service
COPY target/*.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]