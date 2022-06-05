# Calculo API
[![CI](https://github.com/Captainmango/calculoAPI/actions/workflows/ci.yml/badge.svg)](https://github.com/Captainmango/calculoAPI/actions/workflows/ci.yml)

## Background

This project is a living learning project for Spring Boot, Domain Driven Design and TDD. I've done my best to follow patterns outlined by those concepts in this project in order to further my understanding. This is a living project and will change considerably over its lifetime, so any feedback is greatly appreciated. The project uses Java 11 and will be upgraded at some point to 17 as that is the new LTS at the time of writing. The project uses Maven and Docker and has a Makefile to make some commands simpler. There are migrations that need to be run when the app is started locally. Spring will run these automatically, but still check that these have run successfully.

## Running the project

First, run the command:
```bash
$ make build-up
```

This will run the tests and build the docker image to run the application locally. The app has a db image inside the docker-compose file, so you only need to run this command if building from scratch.

Then, we should check the migrations have run successfully. We can do this with FlyWay.

```bash
$ mvn flyway:info
```

If the migrations where successful, they will show up as such in the table output. If there are any issues, run the following:

```bash
$ mvn flyway:validate
```

This will check what migrations have run against the database schema directly. If there are any errors, this will show them in your terminal without changing the database.

To run the migrations use the following command:
```bash
$ mvn flyway:migrate
```

```bash
$ make down
```

Will stop the docker containers and network. The database has a persistent volume, so you may see the seed migration appear to run multiple times. This is not happening and is only appearing due to the migration being repeatable. It will only run if you manually run the migration pack again. This way, you can keep data persistent or flush as needed simplest way to achieve this is to use `$ make re-up` to restart all the containers and then run the migrations again manually. Be sure when you want to reseed though, this cannot be undone.

## Notes

The tests use fixture data that very closely matches the database seed file. If tests are failing, this is likely because you do not have the correct database driver installed. To fix this, run `$ mvn clean` this will install all the maven dependencies locally.
