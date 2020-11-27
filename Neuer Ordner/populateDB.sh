#!/usr/bin/env bash
echo "Initialize database tables for initial deployment"
docker-compose exec -T postgres psql -d immomio_2 -U postgres -f ./docker/populate.sql
docker-compose exec -T postgres psql -d immomio_2-test -U postgres -f ./docker/populate.sql