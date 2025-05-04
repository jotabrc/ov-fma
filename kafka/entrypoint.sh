#!/bin/bash
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

wait_for_port "zookeeper" 2181

#BROKER_ID=$(echo "$HOSTNAME" | grep -o '[0-9]*' | head -1)
#export KAFKA_BROKER_ID=${BROKER_ID:-1}
#export BROKER_HOST=$(hostname -f)
#export KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://${BROKER_HOST}:9092"
exec /etc/confluent/docker/run