#!/bin/bash

echo "Starting HDeskTickets"
echo "Please wait..."
echo "You can access the application at http://localhost:8080/hdesktickets"
echo "You can access the documentation at http://localhost:8080/hdesktickets/swagger-ui.html"
echo "You can access the documentation at http://localhost:8080/hdesktickets/v3/api-docs"
echo "You can access the documentation at http://localhost:8080/hdesktickets/v3/api-docs.yaml"
echo "You can access the documentation at http://localhost:8080/hdesktickets/v3/api-docs.json"

echo "Setting up environment variables..."

HDT_API_PROFILE='production'
DATASOURCE_URL='localhost:5433/hdesktickets_db_dev'
DATASOURCE_USERNAME='hdesktickets'
DATASOURCE_PASSWORD='secret'
HDT_API_RSA_PUBLIC_KEY=""
HDT_API_RSA_PRIVATE_KEY=""
HDT_JAR='target/help_desk_tickets-0.0.1-SNAPSHOT.jar'


echo "Running HDeskTickets..."
java -DHDT_API_PROFILE=$HDT_API_PROFILE \
    -DDATASOURCE_URL=$DATASOURCE_URL \
    -DDATASOURCE_USERNAME=$DATASOURCE_USERNAME \
    -DDATASOURCE_PASSWORD=$DATASOURCE_PASSWORD \
    -DHDT_API_RSA_PUBLIC_KEY=$HDT_API_RSA_PUBLIC_KEY \
    -DHDT_API_RSA_PRIVATE_KEY=$DHDT_API_RSA_PRIVATE_KEY \
    -jar $HDT_JAR;