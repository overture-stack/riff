#!/bin/bash

### =================================================== ###
#     Reference file for server environment variables     #
### =================================================== ###

# === Install Path
export RIFF_INSTALL_PATH=

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

# === VAULT CONFIG
# Leave all below empty if not using VAULT
export VAULT_APPLICATION_NAME=development/overture/riff
export RIFF_VAULT_URI=
export RIFF_VAULT_SCHEME=http
export RIFF_VAULT_HOST=localhost
export RIFF_VAULT_PORT=8200
#leave IAM Role blank if using Token authentication
export RIFF_IAM_ROLE=
#leave Token blank if using IAM Role
export VAULT_TOKEN=
