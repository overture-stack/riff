#!/bin/bash

### =================================================== ###
#     Reference file for server environment variables     #
### =================================================== ###

# === Install Path
export RIFF_INSTALL_PATH=
export RIFF_KEYSTORE_PATH=

# === DB Config
export RIFF_DB=
export RIFF_DB_HOST=
export RIFF_DB_PORT=

# Leave DB_USER AND DB_PASS empty if using VAULT
export RIFF_DB_USER=
export RIFF_DB_PASS=

# === App Server Config
export RIFF_ACTIVE_PROFILES="default"
export RIFF_SERVER_PORT=8081

# Leave IDs and Secrets empty if using VAULT
export RIFF_SERVER_GOOGLE_CLIENT_IDS=""
export RIFF_SERVER_FACEBOOK_APP_ID=""
export RIFF_SERVER_FACEBOOK_SECRET=""

# === VAULT CONFIG
# Leave all below empty if not using VAULT
export VAULT_APPLICATION_NAME="development/oicr/riff"
export RIFF_VAULT_URI=
export RIFF_VAULT_SCHEME=
export RIFF_VAULT_HOST=
export RIFF_VAULT_PORT=
#leave IAM Role blank if using Token authentication
export RIFF_IAM_ROLE=
#leave Token blank if using IAM Role
export VAULT_TOKEN=
