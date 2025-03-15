#!/bin/sh
sed "s/\${PORT}/$PORT/g; s/\${BACKEND_URL}/$BACKEND_URL/g" /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf
nginx -g 'daemon off;'