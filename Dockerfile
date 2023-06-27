FROM openjdk:17-alpine
MAINTAINER saimone
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]