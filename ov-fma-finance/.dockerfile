FROM eclipse-temurin:17-jdk-alpine
WORKDIR /finance-service
COPY target/*.jar finance-service.jar
ENTRYPOINT ["java", "-jar", "finance-service.jar"]