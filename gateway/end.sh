#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

openresty -p "${pwd}" -c nginx.conf -s stop
echo "Gateway stopped"