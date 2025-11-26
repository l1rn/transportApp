#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd SCRIPT_DIR

if [ -f "../../.env" ]; then
    export $(grep -v '^#' ../../.env | xargs)
    echo "Loaded env from /transportApp"
else
    echo "No env found, using default"
    export GATEWAY_PORT=8080
    export BACKEND_PORT=8081
    export FRONTEND_PORT=8082
fi

exec openresty -p "$(pwd)" -c nginx.conf