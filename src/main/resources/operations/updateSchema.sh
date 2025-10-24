#!/bin/sh

readonly IP_ADDRESS=192.168.28.21
readonly APP_NAME=scadaleva
readonly DB_NAME="${APP_NAME}"
readonly DB_USER_NAME="${DB_NAME}"
readonly DB_USER_PASSWORD=123Stella!!!


# Connect to production server, make dump and save backup on local developer machine
#ssh -p "${EXTERNAL_SSH_PORT}" mysql@"${IP_ADDRESS}" '\
#mariadb-dump -u '"${APP_NAME}"' -h localhost '"${DB_NAME}"' | gzip > initial.gz'
#scp -P "${EXTERNAL_SSH_PORT}" mysql@"${IP_ADDRESS}":initial.gz "${BACKUP_FOLDER}"/"${APP_NAME}""${TODAY}".gz

# Backup of DB (only data)
mariadb-dump "${DB_NAME}" --no-create-info | gzip > initial.gz

# Uncompress
gzip --uncompress initial.gz

# Restore DB
mariadb "${DB_NAME}" < initial

# Backup of DB (schema and data)
mariadb-dump "${DB_NAME}" | gzip > initial.gz
