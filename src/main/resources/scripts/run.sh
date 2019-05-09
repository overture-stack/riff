#!/bin/bash

# === Add IAM profile
export RIFF_IAM_PROFILE=$RIFF_ACTIVE_PROFILES,db,app,iam

# === Add token profile
export RIFF_TOKEN_PROFILE=$RIFF_ACTIVE_PROFILES,db,app,token


# check whether vault is setup or not
if [ -z "$RIFF_VAULT_URI" ]
then
    java -jar $RIFF_INSTALL_PATH/install/RIFF.jar \
        --spring.profiles.active=$RIFF_ACTIVE_PROFILES \
        --spring.datasource.url="jdbc:postgresql://$RIFF_DB_HOST:$RIFF_DB_PORT/$RIFF_DB?stringtype=unspecified" \
        --spring.datasource.username="$RIFF_DB_USER" \
        --spring.datasource.password="$RIFF_DB_PASS" \
        --server.port=$RIFF_SERVER_PORT \
        --auth.jwt.publicKeyUrl=$EGO_URL
else
    if [ -z "$VAULT_TOKEN" ]
    then
        echo "Running with IAM role" $RIFF_IAM_ROLE
        java -jar $RIFF_INSTALL_PATH/install/RIFF.jar \
            --spring.profiles.active=$RIFF_IAM_PROFILE \
            --spring.datasource.url=jdbc:postgresql://$RIFF_DB_HOST:$RIFF_DB_PORT/$RIFF_DB \
            --server.port=$RIFF_SERVER_PORT \
            --spring.application.name=$RIFF_VAULT_APPLICATION_NAME \
            --spring.cloud.vault.uri=$RIFF_VAULT_URI \
            --spring.cloud.vault.scheme=$RIFF_VAULT_SCHEME \
            --spring.cloud.vault.host=$RIFF_VAULT_HOST \
            --spring.cloud.vault.port=$RIFF_VAULT_PORT \
            --spring.cloud.vault.aws-iam.role=$RIFF_IAM_ROLE \
            --auth.jwt.publicKeyUrl=$EGO_URL

    else
        echo "Running with Vault token"
        java -jar $RIFF_INSTALL_PATH/install/RIFF.jar \
            --spring.profiles.active=$RIFF_TOKEN_PROFILE \
            --spring.datasource.url=jdbc:postgresql://$RIFF_DB_HOST:$RIFF_DB_PORT/$RIFF_DB \
            --server.port=$RIFF_SERVER_PORT \
            --spring.application.name=$RIFF_VAULT_APPLICATION_NAME \
            --spring.cloud.vault.uri=$RIFF_VAULT_URI \
            --spring.cloud.vault.scheme=$RIFF_VAULT_SCHEME \
            --spring.cloud.vault.host=$RIFF_VAULT_HOST \
            --spring.cloud.vault.port=$RIFF_VAULT_PORT \
            --spring.cloud.vault.token=$VAULT_TOKEN \
            --auth.jwt.publicKeyUrl=$EGO_URL
    fi
fi
