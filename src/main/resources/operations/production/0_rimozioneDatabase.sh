#!/usr/bin/env bash

# Elimina le connessioni esistenti sul database
psql -c "ALTER DATABASE ${DB_NAME} CONNECTION LIMIT 1;"
psql -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname LIKE '${DB_NAME}';"

# Rimuove il database
psql -c "DROP DATABASE ${DB_NAME};"

