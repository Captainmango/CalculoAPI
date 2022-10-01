COMPOSE ?= docker-compose
DOCKER ?= docker
MVN ?= mvn
MVN_RUN ?= ${MVN} spring-boot:run
MVN_BUILD ?= ${MVN} -Dmaven.test.skip=true clean install
MVN_TEST_BUILD ?= ${MVN} clean package
MVN_TEST ?= ${MVN} test

# Start the app
start-app:
		${MVN_RUN}

# Start the database
start-db:
		${COMPOSE} up db --detach

# build and test then start docker containers
build-up:
		${MVN_TEST_BUILD}
		${DOCKER} build -t calculo-api -f build/localdev/Dockerfile .
		${COMPOSE} up --detach

# stop docker containers via compose
down:
		${COMPOSE} down

# start docker containers via compose
up:
		${COMPOSE} up --detach

# tear down then start containers via compose
re-up:
		${COMPOSE} down
		${COMPOSE} up --detach

# Run whole test suite
check:
		${MVN_TEST}