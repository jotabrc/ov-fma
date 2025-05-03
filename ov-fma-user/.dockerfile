FROM eclipse-temurin:17-jdk-alpine
WORKDIR /user-service
COPY target/*.jar user-service.jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]