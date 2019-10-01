#!/bin/bash

# stop on error
set -e

if [ -z `docker ps -q --no-trunc | grep $(docker-compose ps -q postgres)` ]; then
 echo "docker-compose is not running. starting container"
 docker-compose up -d
else
 echo "docker-compose is running already"
fi