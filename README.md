# Calculo API
[![CI](https://github.com/Captainmango/calculoAPI/actions/workflows/ci.yml/badge.svg)](https://github.com/Captainmango/calculoAPI/actions/workflows/ci.yml)

## Background

This project is a living learning project for Spring Boot, Domain Driven Design and TDD. I've done my best to follow patterns outlined by those concepts in this project in order to further my understanding. This is a living project and will change considerably over its lifetime, so any feedback is greatly appreciated. The project uses Java 11 and will be upgraded at some point to 17 as that is the new LTS at the time of writing. The project uses Maven and Docker and has a Makefile to make some commands simpler. There are migrations that need to be run when the app is started locally. Spring will run these automatically, but still check that these have run successfully.

## System dependencies
If you're running the project locally, these are the dependencies used and their versions:
- Java 11 (OpenJDK)
- Maven 3.8.4

If you're using Docker, the Dockerfile (located in build/localdev) is set up to do a multistage build using:
- Java 11 (OpenJDK)
- Maven 3.8.6

## Running the project
### Environment Variables
You will need a .env file in the root of the project. This will be automatically picked up by Spring Boot and by Docker Compose when the make commands are run. There is an example included in the project with the necessary variables to be included. N.B. DATABASE_URL is not used, but is included as it is present in the Production environment, the app follows 12 Factor principles. 

### Docker
The commands referenced here are inside the Makefile in the root of the project. If you use Docker to build the app, the app will not be compiled locally. There is a multistage build that produces the JAR file that is then loaded into the container. The process to start the app is as follows:

First, run the command:
```bash
$ make build-up
```

This will run the tests and build the docker image to run the application locally. It will also start the database and create a network and volume.

To run the migrations use the following command:
```bash
$ mvn flyway:migrate
```

We should check the migrations have run successfully. We can do this with FlyWay.

```bash
$ mvn flyway:info
```

If the migrations where successful, they will show up in a table output. If there are any issues, run the following:

```bash
$ mvn flyway:validate
```

This will check what migrations have run against the database schema directly. If there are any errors, this will show them in your terminal without changing the database. It will also validate the connection to the database.

To stop all containers running use the command:
```bash
$ make down
```

### Local dev
This will run the app locally on your machine. To ensure the app runs correctly, first run

```bash
$ mvn clean
$ mvn install
```
This will clean any artefacts that may be lurking (there shouldn't be any, but this is for sanity), then will install all the dependencies required for the application.

The database needs to be started before the application. However, if this isn't done, the app will start, but will not be usable. This is because there is middleware that will run to validate access using a JWT that uses the database.

```bash
$ make start-db
```

Now we need to run the migrations:
```bash
$ mvn flyway:migrate
```

We should check the migrations have run successfully. We can do this with FlyWay.

```bash
$ mvn flyway:info
```

If the migrations where successful, they will show up in a table output. If there are any issues, run the following:

```bash
$ mvn flyway:validate
```

Now the database has been set up correctly, we can start the application:
```bash
$ make start-app
```

This will start the Application using the defaults in the `application.properties` file in `src/main/java/resources` This loads envvars from the .env automatically, so there is no need to change anything here.

The app will create logs in the terminal used to run the application. Using `CTRL + C` will stop the application. To stop the database, run:
```bash
$ make down
```

### Tests
Running the entire test suite is done with the command `$ make check` 

## Makefile Commands
- `$ make start-app` Will start the Spring Boot application
- `$ make start-db` Will start the Database listed in docker-compose.yml
- `$ make build-up` Will build a Docker image for the application and start it. It will also start the database
- `$ make down` Will stop all docker containers listed in the docker-compose.yml running and stop the network
- `$ make up` Will start all docker containers listed in docker-compose.yml N.B. if the server image has not been built, this will error
- `$ make re-up` Stops, then starts the docker containers listed in docker-compose.yml
- `$ make check` Runs the test suite via Maven

## Troubleshooting

### Tests
The tests use fixture data that very closely matches the database seed file. If tests are failing, this is likely because you do not have the correct database driver installed. To fix this, run `$ mvn clean` this will install all the maven dependencies locally.

