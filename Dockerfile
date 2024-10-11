FROM openjdk:21-jdk
MAINTAINER Wojtek03
COPY target/invoice-service-0.0.1-SNAPSHOT.jar invoice-service.jar
ENTRYPOINT ["java", "-jar", "/invoice-service.jar"]