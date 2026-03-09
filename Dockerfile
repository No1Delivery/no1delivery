FROM eclipse-temurin:17-jdk

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV DB_DDL_AUTO=none

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080