#!/bin/bash

echo "Starting HDeskTickets"
echo "Please wait..."
echo "Running docker-compose up"

docker compose -f ./hdesktickets/docker-compose.yml up -d