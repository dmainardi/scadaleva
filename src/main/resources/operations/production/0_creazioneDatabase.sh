#!/usr/bin/env bash

# Crea il database vuoto
createdb --owner=${DB_USER_NAME} --encoding=UTF8 --template=template0 ${DB_NAME}

# Copia i dati sul database di produzione
gzip --uncompress ${DB_INITIAL_FILENAME_COMPRESSED}
pg_restore -d ${DB_NAME} ${DB_INITIAL_FILENAME}
rm ${DB_INITIAL_FILENAME}

