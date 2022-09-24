FROM openjdk:11

ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=database
ENV POSTGRES_USER=user
ENV APP_PORT=8080
ENV APP_SECRET=supersecret
ENV APP_REQUEST_ORIGIN=*

COPY ./target/calculo-api-1.0.0.jar calculo-api-1.0.0.jar
CMD ["java", "-jar", "calculo-api-1.0.0.jar"]