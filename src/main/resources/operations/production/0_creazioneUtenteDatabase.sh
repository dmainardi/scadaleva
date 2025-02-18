#!/usr/bin/env bash

# Crea l'utente di produzione
export db_user_password=$(cut -d "=" -f 2 dbUserPassword)
mysql -e "CREATE USER ${DB_USER_NAME}@'localhost' IDENTIFIED BY '$db_user_password';"
rm dbUserPassword

# Crea l'utente utile al programma di contabilità
export db_accounting_user_password=$(cut -d "=" -f 2 dbAccountingUserPassword)
mysql -e "CREATE USER ${DB_ACCOUNTING_USER_NAME} IDENTIFIED BY '$db_accounting_user_password';"
rm dbAccountingUserPassword

# Aggiunge la regola in modo che l'utente DB_ACCOUNTING_USER_NAME possa accedere al database DB_NAME dalla stessa sotto-rete
#echo "host ${DB_NAME} ${DB_ACCOUNTING_USER_NAME} 192.168.1.0/24 md5" >> /etc/postgresql/15/main/pg_hba.conf

# /etc/mysql/mariadb.conf.d/50-server.cnf
# la riga
# bind-address            = 127.0.0.1
# deve diventare
# bind-address            = 0.0.0.0
# perché altrimenti non va
