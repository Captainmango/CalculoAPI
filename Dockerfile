FROM openjdk:11

COPY .env .env

COPY ./target/calculo-api-1.0.0.jar calculo-api-1.0.0.jar
CMD ["java", "-jar", "calculo-api-1.0.0.jar"]