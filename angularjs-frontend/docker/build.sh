#!/bin/bash
set -e

#Build APP
echo "Building Kanban Board AngularJS Frontend..."

rm -fr ../../prebuilt-angularjs-client

cd ../
rm -f cidfile
docker build -t kanban-board-angularjs-frontend-gulper .
docker run --cidfile cidfile kanban-board-angularjs-frontend-gulper
CONTAINER_ID=$(cat cidfile)
docker cp $CONTAINER_ID:/app/prebuilt-angularjs-client ../
docker rm $CONTAINER_ID

echo "Build successfuly completed"
