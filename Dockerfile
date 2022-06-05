FROM openjdk:11
# this is a nasty hack. Only doing this for dev purposes.
# Will never deploy an image with a dockerfile like this
COPY .env .env
COPY ./target/calculo-api-1.0.0.jar calculo-api-1.0.0.jar
CMD ["java", "-jar", "calculo-api-1.0.0.jar"]