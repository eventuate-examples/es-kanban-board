#! /bin/bash -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo $DIR

cp ../libs/*.jar .

cp $DIR/Dockerfile .

docker build -t eventuate_kanban_${1?} .

