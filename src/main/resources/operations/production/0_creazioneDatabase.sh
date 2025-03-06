#!/usr/bin/env bash

# Crea il database vuoto
mysql -e "CREATE DATABASE ${DB_NAME};"

# Assegna tutti i privilegi all'utente del database di produzione
mysql -e "GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO ${DB_USER_NAME};"

# Copia i dati sul database di produzione
gzip --uncompress ${DB_INITIAL_FILENAME_COMPRESSED}
mariadb ${DB_NAME} < ${DB_INITIAL_FILENAME}
rm ${DB_INITIAL_FILENAME}

# Assegna i privilegi in sola lettura solo per la tabella 'ProduzioneGestionale' all'utente utile al programma di contabilità
mysql -e "GRANT SELECT ON ${DB_NAME}.produzione TO ${DB_ACCOUNTING_USER_NAME};"

# Per fare il backup del database usare
#mariadb-dump ${DB_NAME} | gzip > ${DB_INITIAL_FILENAME_COMPRESSED}