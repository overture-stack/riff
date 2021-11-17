#!/bin/bash

### =================================================== ###
#     Reference file for server environment variables     #
### =================================================== ###

# === Install Path
export RIFF_INSTALL_PATH=

# === Cors Allowed domains
export RIFF_CORS_ALLOWED_DOMAINS=

# === DB Config
export RIFF_DB=riff
export RIFF_DB_HOST=localhost
export RIFF_DB_PORT=5432

# Leave DB_USER AND DB_PASS empty if using VAULT
export RIFF_DB_USER=
export RIFF_DB_PASS=

# === App Server Config
export RIFF_ACTIVE_PROFILES="default"
export RIFF_SERVER_PORT=8081

# === Keycloak configs
export KEYCLOAK_REALM=
export KEYCLOAK_URL=
export KEYCLOAK_CLIENT=