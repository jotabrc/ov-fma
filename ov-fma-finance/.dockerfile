FROM eclipse-temurin:17-jdk-alpine
WORKDIR /finance-service
COPY target/*.jar finance-service.jar

USER root
RUN apk add --no-cache netcat-openbsd bash

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]