#!/bin/bash
set -e

OD=$(pwd)

docker-compose up -d mongodb

cd ..
./gradlew :standalone-service:clean :standalone-service:assemble

cd $OD

docker-compose stop

echo "Build successfuly completed"
