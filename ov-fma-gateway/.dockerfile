FROM eclipse-temurin:17-jdk-alpine
WORKDIR /gateway-service
COPY target/*.jar gateway-service.jar
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]