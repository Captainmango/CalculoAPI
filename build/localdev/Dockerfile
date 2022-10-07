# Install deps and package JAR
FROM maven:3.8.6-ibm-semeru-11-focal as maven-build
COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package -DskipTests

# Set up env and build the container
FROM eclipse-temurin:11

COPY --from=maven-build /app/target/calculo-api-1.0.0.jar /web-app/calculo-api-1.0.0.jar
WORKDIR /web-app

# Postgres vars
ARG PGUSER=admin
ARG PGPASSWORD=
ARG PGDATABASE=calculo_db
ARG PGHOST=db
ARG PGPORT=5432
ARG DATABASE_URL=postgresql://db/calculo_db

ENV PGUSER=$PGUSER
ENV PGPASSWORD=$PGPASSWORD
ENV PGDATABASE=$PGDATABASE
ENV PGHOST=$PGHOST
ENV PGPORT=$PGPORT
ENV DATABASE_URL=$DATABASE_URL

# App vars
ARG PORT=8080
ARG APP_SECRET=thisisasecret
ARG APP_REQUEST_ORIGIN=*
ARG APP_ENVIRONMENT=dev

ENV PORT=$PORT
ENV APP_SECRET=$APP_SECRET
ENV APP_REQUEST_ORIGIN=$APP_REQUEST_ORIGIN
ENV APP_ENVIRONMENT=$APP_ENVIRONMENT

EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "/web-app/calculo-api-1.0.0.jar"]