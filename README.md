# Calculo API

## Background

This project is a living learning project for Spring Boot, Domain Driven Design and TDD. I've done my best to follow patterns outlined by those concepts in this project in order to further my understanding. This is a living project and will change considerably over its lifetime, so any feedback is greatly appreciated. The project uses Java 11 and will be upgraded at some point to 17 as that is the new LTS at the time of writing.


## Running the project

First, run the command:
```bash
$ ./mvn install
```

This will run the tests and install all dependencies. Once done, you can run:

```bash
$ ./mvn spring-boot:run
```

To start the project in development mode. Alternatively, this can be started using a run configuration in your IDE of choice.

## Notes

The tests use very basic factories to provide users and expenses for testing. There is an entirely separate configuration for running tests that drops and remakes the db on every run using H2. The console is not available during tests due to the spring security set up. This can be turned off in WebSecurityConfig.java