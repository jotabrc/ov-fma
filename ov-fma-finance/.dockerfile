FROM eclipse-temurin:17
WORKDIR /finance-service
COPY target/*.jar finance-service.jar
ENTRYPOINT ["java", "-jar", "finance-service.jar"]