FROM eclipse-temurin:17
WORKDIR /auth-service
COPY target/*.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]