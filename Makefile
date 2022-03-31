COMPOSE ?= docker-compose
DOCKER ?= docker
MVN ?= mvn
MVN_BUILD ?= ${MVN} clean package
MVN_TEST ?= ${MVN} test

# build API image
build:
		${MVN_BUILD}
		${DOCKER} image build -t calculo-api .

# build image and start docker container
build-up:
		build
		${COMPOSE} up

# stop docker container
down:
		${COMPOSE} down

# start docker container
up:
		${COMPOSE} up

re-up:
		down
		build-up

check:
		${MVN_TEST}