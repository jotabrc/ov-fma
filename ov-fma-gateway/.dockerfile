FROM eclipse-temurin:17
WORKDIR /gateway-service
COPY target/*.jar gateway-service.jar
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]