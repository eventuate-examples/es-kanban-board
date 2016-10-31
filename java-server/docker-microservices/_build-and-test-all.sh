#! /bin/bash

set -e

ORIGINAL_DIR=$(pwd)

if [ -z "$DOCKER_HOST_IP" ] ; then
  if [ -z "$DOCKER_HOST" ] ; then
    export DOCKER_HOST_IP=`hostname`
  else
    echo using ${DOCKER_HOST?}
    XX=${DOCKER_HOST%\:*}
    export DOCKER_HOST_IP=${XX#tcp\:\/\/}
  fi
  echo set DOCKER_HOST_IP $DOCKER_HOST_IP
fi

if [ -z "$SPRING_DATA_MONGODB_URI" ] ; then
  export SPRING_DATA_MONGODB_URI=mongodb://${DOCKER_HOST_IP?}/mydb
  echo Set SPRING_DATA_MONGODB_URI $SPRING_DATA_MONGODB_URI
fi

DOCKER_COMPOSE="docker-compose -p kanban-board"

if [ "$1" = "-f" ] ; then
  shift;
  DOCKER_COMPOSE="$DOCKER_COMPOSE -f ${1?}"
  shift
fi

if [ "$1" = "--use-existing" ] ; then
  shift;
else
  ${DOCKER_COMPOSE?} stop
  ${DOCKER_COMPOSE?} rm -v --force
fi

NO_RM=false

if [ "$1" = "--no-rm" ] ; then
  NO_RM=true
  shift
fi

JS_TESTS_RUN=false

if [ "$1" = "--run-js-tests" ] ; then
  JS_TESTS_RUN=true
  shift
fi

${DOCKER_COMPOSE?} up -d mongodb $EXTRA_INFRASTRUCTURE_SERVICES

export SERVICE_HOST=$DOCKER_HOST_IP
export DOCKER_PORT=8080

cd ..

./gradlew $* build -x :e2e-test:test

cd ./docker-microservices

if [ -z "$EVENTUATE_LOCAL" ] && [ -z "$EVENTUATE_API_KEY_ID" -o -z "$EVENTUATE_API_KEY_SECRET" ] ; then
  echo You must set EVENTUATE_API_KEY_ID and  EVENTUATE_API_KEY_SECRET
  exit -1
fi


${DOCKER_COMPOSE?} up -d

./wait-for-services.sh $DOCKER_HOST_IP 8080 8081 8082 8083 8084

set -e

cd ..

./gradlew -P ignoreE2EFailures=false $* :e2e-test:cleanTest :e2e-test:test

cd ./docker-microservices

if [ $JS_TESTS_RUN = true ] ; then
  cd ../../e2e-tests

  npm i
  xvfb-run --server-args="-screen 0 1440x890x24"  npm run test

  cd $ORIGINAL_DIR
fi

if [ $NO_RM = false ] ; then
  ${DOCKER_COMPOSE?} stop
  ${DOCKER_COMPOSE?} rm -v --force
fi
