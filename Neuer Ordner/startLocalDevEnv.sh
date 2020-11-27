#!/usr/bin/env bash

export LOCAL_POSTGRES_DATA_DIR=~/postgresdata
echo "Creating local directory for Postgres data: $LOCAL_POSTGRES_DATA_DIR"
mkdir -p $LOCAL_POSTGRES_DATA_DIR

echo "Building Docker containers..."
docker-compose up --build -d

echo "Waiting for Docker containers to finish setting up..."
./waitForContainerSetup.sh
docker-compose ps

./populateDB.sh


