#!/usr/bin/env bash

# Rimuove l'eventuale vecchio dominio di produzione
${AS_HOME}/bin/asadmin delete-domain ${AS_DOMAIN_NAME}
# Crea il dominio di produzione
${AS_HOME}/bin/asadmin create-domain --user admin --nopassword --template ${AS_HOME}/glassfish/common/templates/gf/production-domain.jar ${AS_DOMAIN_NAME}
# Avvia l'application server col dominio di produzione
${AS_HOME}/bin/asadmin start-domain ${AS_DOMAIN_NAME}
# Crea l'alias password per il collegamento col databse
${AS_HOME}/bin/asadmin --passwordfile ~/dbUserPassword create-password-alias ${AS_PASSWORD_ALIAS_NAME}
rm ~/dbUserPassword
# Crea l'alias password per il collegamento col databse in sola lettura del programma di contabilità
${AS_HOME}/bin/asadmin --passwordfile ~/dbReadOnlyUserPassword create-password-alias ${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}
rm ~/dbReadOnlyUserPassword
# Rimuove altri file che contengono password
rm ~/*Password
# Aggiunge le librerie per l'applicazione
${AS_HOME}/bin/asadmin add-library ~/*.jar
rm ~/*.jar
${AS_HOME}/bin/asadmin restart-domain ${AS_DOMAIN_NAME}
# Crea JDBC connection pool
${AS_HOME}/bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=org.postgresql.ds.PGSimpleDataSource \
--restype=javax.sql.DataSource \
--validationmethod=auto-commit \
--allownoncomponentcallers=false \
--nontransactionalconnections=false \
--driverclassname=org.postgresql.Driver \
--property user=${DB_USER_NAME}:\
password=\$\{ALIAS=${AS_PASSWORD_ALIAS_NAME}\}:\
databaseName=${DB_NAME}:\
serverName=localhost:\
portNumber=5432:\
url=jdbc\\:postgresql\\://localhost\\:5432/${DB_NAME} \
postgres_${APP_NAME}_pool
# Crea JDBC resource
${AS_HOME}/bin/asadmin create-jdbc-resource --connectionpoolid postgres_${APP_NAME}_pool jdbc/postgres_${APP_NAME}
# Crea JDBC connection pool for Accounting database
${AS_HOME}/bin/asadmin create-jdbc-connection-pool \
--datasourceclassname=com.mysql.cj.jdbc.MysqlDataSource \
--restype=javax.sql.DataSource \
--property user=${DB_READ_ONLY_USER_NAME}:\
password=\$\{ALIAS=${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}\}:\
DatabaseName=${DB_READ_ONLY_NAME}:\
ServerName=${IP_READ_ONLY_ADDRESS}:\
portNumber=${TCP_READ_ONLY_PORT} \
mysql_${APP_NAME}_pool
# Crea JDBC resource for Accounting database
${AS_HOME}/bin/asadmin create-jdbc-resource --connectionpoolid mysql_${APP_NAME}_pool jdbc/mysql_${APP_NAME}
