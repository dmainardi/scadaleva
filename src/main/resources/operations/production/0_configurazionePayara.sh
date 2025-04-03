#!/usr/bin/env bash

# Rimuove l'eventuale vecchio dominio di produzione
${AS_HOME}/bin/asadmin delete-domain ${AS_DOMAIN_NAME}
# Crea il dominio di produzione
${AS_HOME}/bin/asadmin create-domain --user admin --nopassword --template ${AS_HOME}/glassfish/common/templates/gf/production-domain.jar ${AS_DOMAIN_NAME}
# Avvia l'application server col dominio di produzione
${AS_HOME}/bin/asadmin start-domain ${AS_DOMAIN_NAME}
# Crea l'alias password per il collegamento col databse principale
${AS_HOME}/bin/asadmin --passwordfile ~/dbUserPassword create-password-alias ${AS_PASSWORD_ALIAS_NAME}
rm ~/dbUserPassword
# Crea l'alias password per il collegamento col databse in sola lettura del gestionale interno
${AS_HOME}/bin/asadmin --passwordfile ~/dbReadOnlyUserPassword create-password-alias ${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}
rm ~/dbReadOnlyUserPassword
# Rimuove altri file che contengono password
rm ~/*Password
# Aggiunge le librerie per l'applicazione
${AS_HOME}/bin/asadmin add-library ~/*.jar
rm ~/*.jar
${AS_HOME}/bin/asadmin restart-domain ${AS_DOMAIN_NAME}
# Crea JDBC connection pool associata al databse principale
${AS_HOME}/bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=com.mysql.cj.jdbc.MysqlDataSource \
--restype=javax.sql.DataSource \
--property \
user=${DB_USER_NAME}:\
password=\$\{ALIAS=${AS_PASSWORD_ALIAS_NAME}\}:\
databaseName=${DB_NAME}:\
serverName=localhost:\
portNumber=${TCP_PORT}:\
useSSL=false \
mysql_${APP_NAME}_pool
# Crea JDBC resource associata al databse principale
${AS_HOME}/bin/asadmin create-jdbc-resource --connectionpoolid mysql_${APP_NAME}_pool jdbc/mysql_${APP_NAME}
# Crea JDBC connection pool associata al databse in sola lettura del gestionale interno
${AS_HOME}/bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=com.mysql.cj.jdbc.MysqlDataSource \
--restype=javax.sql.DataSource \
--property \
user=${DB_READ_ONLY_USER_NAME}:\
password=\$\{ALIAS=${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}\}:\
DatabaseName=${DB_READ_ONLY_NAME}:\
ServerName=${IP_READ_ONLY_ADDRESS}:\
portNumber=${TCP_READ_ONLY_PORT} \
mysql_readOnly_${APP_NAME}_pool
# Crea JDBC resource associata al databse in sola lettura del gestionale interno
${AS_HOME}/bin/asadmin create-jdbc-resource --connectionpoolid mysql_readOnly_${APP_NAME}_pool jdbc/mysql_readOnly_${APP_NAME}
# Crea JDBC connection pool associata al primo databse di Access (ArchivioStorico)
${AS_HOME}/bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=net.ucanaccess.jdbc.UcanaccessDataSource \
--restype=javax.sql.DataSource \
--property \
user=${DB_ACCESS_READ_ONLY_USER_NAME}:\
Password=superSecretPassowordWorldWide:\
AccessPath=/home/${AS_USER_NAME}/${APP_NAME}/${DB_ACCESS_READ_ONLY_FOLDER_NAME}/ArchivioStorico.Mdb:\
LoginTimeout=0 \
access_readOnly_${APP_NAME}_archivioStorico_pool
# Crea JDBC resource associata al primo databse di Access (ArchivioStorico)
${AS_HOME}/bin/asadmin create-jdbc-resource --connectionpoolid access_readOnly_${APP_NAME}_archivioStorico_pool jdbc/access_readOnly_${APP_NAME}_archivioStorico_pool
# Crea JDBC connection pool associata al secondo databse di Access (archiviolavoro)
${AS_HOME}/bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=net.ucanaccess.jdbc.UcanaccessDataSource \
--restype=javax.sql.DataSource \
--property \
user=${DB_ACCESS_READ_ONLY_USER_NAME}:\
Password=superSecretPassowordWorldWide:\
AccessPath=/home/${AS_USER_NAME}/${APP_NAME}/${DB_ACCESS_READ_ONLY_FOLDER_NAME}/archiviolavoro.Mdb:\
LoginTimeout=0 \
access_readOnly_${APP_NAME}_archivioLavoro_pool
# Crea JDBC resource associata al secondo databse di Access (archiviolavoro)
${AS_HOME}/bin/asadmin create-jdbc-resource --connectionpoolid access_readOnly_${APP_NAME}_archivioLavoro_pool jdbc/access_readOnly_${APP_NAME}_archivioLavoro_pool
