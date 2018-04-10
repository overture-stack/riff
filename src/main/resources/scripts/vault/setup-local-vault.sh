#!/usr/bin/env bash

echo 'To test vault locally: download vault from: https://www.vaultproject.io/downloads.html and unzip in this folder'
echo 'start vault server using : ./vault server -config ./vault.conf'
echo 'Once server is running, execute this file again to setup required keys in vault'

# Vault server address
export VAULT_ADDR=http://localhost:8200

# initialize vault
export OUTPUT=$(./vault operator init)

# set token
export VAULT_TOKEN=$(echo $(echo $OUTPUT | awk -F'Token: ' '{print$2}' | awk -F' Vault' '{print $1}'))

echo 'User this token in bootstrap-token.properties:' $VAULT_TOKEN

# grab all unseal keys
export VAULT_UNSEAL_KEY1=$(echo $(echo $OUTPUT | awk -F'Unseal Key 1:' '{print$2}' | awk -F' Unseal' '{print $1}'))
export VAULT_UNSEAL_KEY2=$(echo $(echo $OUTPUT | awk -F'Unseal Key 2:' '{print$2}' | awk -F' Unseal' '{print $1}'))
export VAULT_UNSEAL_KEY3=$(echo $(echo $OUTPUT | awk -F'Unseal Key 3:' '{print$2}' | awk -F' Unseal' '{print $1}'))

# unseal vault
./vault operator unseal $VAULT_UNSEAL_KEY1
./vault operator unseal $VAULT_UNSEAL_KEY2
./vault operator unseal $VAULT_UNSEAL_KEY3

./vault write secret/development/oicr/riff/dev spring.datasource.username=postgres
./vault read /secret/development/oicr/riff/dev