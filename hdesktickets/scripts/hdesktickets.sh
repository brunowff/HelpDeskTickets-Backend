#!/bin/bash

echo "Starting HDeskTickets"
echo "Please wait..."

echo "Setting up environment variables..."

source hdesktickets/scripts/hdesktickets.conf

export HDT_API_PROFILE
export HDT_PORT
export HDT_DATASOURCE_URL
export HDT_DATASOURCE_USERNAME
export HDT_DATASOURCE_PASSWORD
export HDT_API_RSA_PUBLIC_KEY
export HDT_API_RSA_PRIVATE_KEY
export HDT_JAR

echo "You can access the application at http://localhost:$HDT_PORT/hdesktickets"
echo "You can access the documentation at http://localhost:$HDT_PORT/hdesktickets/swagger-ui.html"
echo "You can access the documentation at http://localhost:$HDT_PORT/hdesktickets/v3/api-docs"
echo "You can access the documentation at http://localhost:$HDT_PORT/hdesktickets/v3/api-docs.yaml"
echo "You can access the documentation at http://localhost:$HDT_PORT/hdesktickets/v3/api-docs.json"


echo "HDT_API_PROFILE=$HDT_API_PROFILE"
echo "HDT_DATASOURCE_URL=$HDT_DATASOURCE_URL"
echo "HDT_DATASOURCE_USERNAME=$HDT_DATASOURCE_USERNAME"
echo "HDT_DATASOURCE_PASSWORD=$HDT_DATASOURCE_PASSWORD"
echo "HDT_API_RSA_PUBLIC_KEY=$HDT_API_RSA_PUBLIC_KEY"
echo "HDT_API_RSA_PRIVATE_KEY=$HDT_API_RSA_PRIVATE_KEY"
echo "HDT_JAR=$HDT_JAR"

echo "Running HDeskTickets..."
java -DHDT_API_PROFILE=$HDT_API_PROFILE \
    -DHDT_DATASOURCE_URL=$HDT_DATASOURCE_URL \
    -DHDT_DATASOURCE_USERNAME=$HDT_DATASOURCE_USERNAME \
    -DHDT_DATASOURCE_PASSWORD=$HDT_DATASOURCE_PASSWORD \
    -DHDT_API_RSA_PUBLIC_KEY=$HDT_API_RSA_PUBLIC_KEY \
    -DHDT_API_RSA_PRIVATE_KEY=$HDT_API_RSA_PRIVATE_KEY \
    -DHDT_PORT=$HDT_PORT \
    -jar $HDT_JAR;