#!/usr/bin/env bash

# Elimina le connessioni esistenti sul database
#mysql -e "ALTER DATABASE ${DB_NAME} CONNECTION LIMIT 1;"
#mysql -e "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname LIKE '${DB_NAME}';"

# Rimuove il database
mysql -e "DROP DATABASE ${DB_NAME};"

