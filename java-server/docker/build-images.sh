#! /bin/bash -e

CURDIR=`pwd`

for dir in api-gateway-service board-command-side-service task-command-side-service board-query-side-service \
  task-query-side-service standalone-service; do

DOCKER_BUILD_DIR=$dir/build/docker

mkdir -p ${DOCKER_BUILD_DIR?}

cd ${DOCKER_BUILD_DIR?}

../../../docker/_build.sh `echo $dir | sed -e 's/-/_/g'`

cd ${CURDIR?}

done
