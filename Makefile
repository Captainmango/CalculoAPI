COMPOSE ?= docker-compose
DOCKER ?= docker
MVN ?= mvn
MVN_BUILD ?= ${MVN} -Dmaven.test.skip=true clean install
MVN_TEST_BUILD ?= ${MVN} clean package
MVN_TEST ?= ${MVN} test

# build API image
build:
		${MVN_BUILD}
		${DOCKER} image build -t calculo-api .

# build image and start docker container
build-up:
		${MVN_BUILD}
		${DOCKER} image build -t calculo-api .
		${COMPOSE} up --detach

# stop docker container
down:
		${COMPOSE} down

# start docker container
up:
		${COMPOSE} up --detach

# tear down then start container
re-up:
		${COMPOSE} down
		${COMPOSE} up --detach

# Run whole test suite
check:
		${MVN_TEST}

# clean build and test image
build-test-up:
		${MVN_TEST_BUILD}
		${DOCKER} image build -t calculo-api .
		${COMPOSE} up --detach