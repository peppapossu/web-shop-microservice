#!/bin/sh
set -e

echo "Registering Debezium connectors..."

curl -X POST http://kafka-connect:8083/connectors \
  -H "Content-Type: application/json" \
  -d @/debezium/order-outbox-connector.json

echo "Done."


#!/bin/bash
set -e

CONNECTOR_NAME="orders-outbox-connector"
CONNECTOR_FILE="/connectors/outbox-connector.json"

# Проверяем, существует ли connector
if curl --silent -f -X GET "http://kafka-connect:8083/connectors/$CONNECTOR_NAME" > /dev/null; then
  echo "Connector '$CONNECTOR_NAME' already exists. Skipping creation."
else
  echo "Creating connector '$CONNECTOR_NAME'..."
  curl -X POST http://kafka-connect:8083/connectors \
       -H "Content-Type: application/json" \
       -d @"$CONNECTOR_FILE"
fi

