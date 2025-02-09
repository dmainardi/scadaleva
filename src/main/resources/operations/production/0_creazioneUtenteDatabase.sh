#!/usr/bin/env bash

# Crea l'utente di produzione
export db_user_passwrord=$(cut -d "=" -f 2 dbUserPassword)
psql -c "CREATE ROLE ${DB_USER_NAME} NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN PASSWORD '$db_user_passwrord' IN ROLE postgres;"
rm dbUserPassword

# Crea l'utente utile al programma di contabilità
export db_accounting_user_passwrord=$(cut -d "=" -f 2 dbAccountingUserPassword)
psql -c "CREATE ROLE ${DB_ACCOUNTING_USER_NAME} NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN PASSWORD '$db_accounting_user_passwrord' IN ROLE ${DB_USER_NAME};"
rm dbAccountingUserPassword

# Aggiunge la regola in modo che l'utente DB_ACCOUNTING_USER_NAME possa accedere al database DB_NAME dalla stessa sotto-rete
echo "host ${DB_NAME} ${DB_ACCOUNTING_USER_NAME} 192.168.1.0/24 md5" >> /etc/postgresql/15/main/pg_hba.conf
