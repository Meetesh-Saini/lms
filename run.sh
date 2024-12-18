#!/bin/bash

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

trap 'kill $(jobs -p)' EXIT

echo 'Running Spring Boot application...'
cd "$SCRIPTPATH/backend" || { echo "Failed to access backend directory"; exit 1; }
# Run Spring Boot in the background
./mvnw spring-boot:run > "$SCRIPTPATH/backend.log" 2>&1 &
SPRING_PID=$!

echo 'Running Angular application...'
cd "$SCRIPTPATH/frontend" || { echo "Failed to access frontend directory"; exit 1; }
# Run Angular in the background
ng serve > "$SCRIPTPATH/frontend.log" 2>&1 &
ANGULAR_PID=$!

echo 'Started both applications. Monitoring their status...'
echo 'Check logs at:'
echo -e "\t$SCRIPTPATH/backend.log\n\t$SCRIPTPATH/frontend.log"

# Loop to concurrently check whether either process has finished
while true; do
    if ! kill -0 $SPRING_PID 2>/dev/null; then
        echo 'Spring Boot application has stopped.'
        break
    fi
    if ! kill -0 $ANGULAR_PID 2>/dev/null; then
        echo 'Angular application has stopped.'
        break
    fi

    sleep 1
done

echo 'Exiting script...'
