#!/usr/bin/env bash

if [ -z "$EVENTUATE_API_KEY_ID" -o -z "$EVENTUATE_API_KEY_SECRET" ] ; then
  echo You must set EVENTUATE_API_KEY_ID and  EVENTUATE_API_KEY_SECRET
  exit -1
fi

echo "arguments" $*

./gradlew $BUILD_AND_TEST_ALL_EXTRA_GRADLE_ARGS $* clean build -xtest

cd ./standalone-service

echo "arguments" $*

../gradlew $BUILD_AND_TEST_ALL_EXTRA_GRADLE_ARGS $* clean copyAngularJS bootRun