#!/bin/bash


java -jar $RIFF_INSTALL_PATH/install/RIFF.jar \
        --spring.profiles.active=$RIFF_ACTIVE_PROFILES \
        --spring.datasource.url="jdbc:postgresql://$RIFF_DB_HOST:$RIFF_DB_PORT/$RIFF_DB?stringtype=unspecified" \
        --spring.datasource.username="$RIFF_DB_USER" \
        --spring.datasource.password="$RIFF_DB_PASS" \
        --spring.cors_allowed_domains="$RIFF_CORS_ALLOWED_DOMAINS" \
        --server.port=$RIFF_SERVER_PORT \
        --keycloak.realm=$KEYCLOAK_REALM \
        --keycloak.auth-server-url=$KEYCLOAK_URL \
        --keycloak.resource=$KEYCLOAK_CLIENT
