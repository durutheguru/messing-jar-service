
FROM openjdk:17

WORKDIR /app

COPY /target/messing-jar-service.jar /app/messing-jar-service.jar

EXPOSE 10101

ENTRYPOINT ["java", "-jar", "messing-jar-service.jar"]

