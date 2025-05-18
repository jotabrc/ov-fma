#!/bin/sh
set -e

wait_for_port() {
  local host=$1
  local port=$2
  echo "Waiting for ${host}:${port} to be available..."
  while ! nc -z $host $port; do
    echo "Still waiting for ${host}:${port}..."
    sleep 5
  done
  echo "${host}:${port} is available!"
}

wait_for_port "auth-postgres-sql" 5432
wait_for_port "config-service" 8084
wait_for_port "kafka-1" 9092
sleep 10
exec java -jar auth-service.jar