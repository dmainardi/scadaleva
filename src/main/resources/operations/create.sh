#!/bin/sh
readonly IDE_WORKSPACE=$HOME/NetBeansProjects
readonly AS_LIBFOLDER=$HOME/payara6/glassfish/domains/domain1/lib
readonly APP_NAME=scadaleva
readonly IP_ADDRESS=192.168.1.167
readonly DB_NAME="${APP_NAME}"
readonly DB_USER_NAME="${APP_NAME}"
readonly DB_USER_PASSWORD=123Stella!!!

readonly IP_READ_ONLY_ADDRESS=192.168.1.200
readonly TCP_READ_ONLY_PORT=3306
readonly DB_READ_ONLY_NAME=axis_files_lev
readonly DB_READ_ONLY_USER_NAME=comandu
readonly DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME=scadaleva-db-readyonly-user-alias

mkdir $HOME/"${APP_NAME}"
mkdir $HOME/"${APP_NAME}"/documents
\
mvn -f "${IDE_WORKSPACE}"/"${APP_NAME}"/pom.xml -DincludeScope=provided -DexcludeGroupIds=jakarta.activation,jakarta.annotation,jakarta.authentication,jakarta.authorization,jakarta.batch,jakarta.ejb,jakarta.el,jakarta.enterprise,jakarta.faces,jakarta.inject,jakarta.interceptor,jakarta.jms,jakarta.json,jakarta.json.bind,jakarta.mail,jakarta.persistence,jakarta.platform,jakarta.resource,jakarta.security.enterprise,jakarta.servlet,jakarta.transaction,jakarta.validation,jakarta.websocket,jakarta.ws.rs -DoutputDirectory=$HOME/"${APP_NAME}"/ dependency:copy-dependencies
\
./asadmin start-domain
./asadmin add-library $HOME/"${APP_NAME}"/*.jar
./asadmin create-jdbc-connection-pool \
--datasourceclassname=org.postgresql.ds.PGSimpleDataSource \
--restype=javax.sql.DataSource \
--validationmethod=auto-commit \
--allownoncomponentcallers=false \
--nontransactionalconnections=false \
--driverclassname=org.postgresql.Driver \
--property user="${DB_USER_NAME}":\
password="${DB_USER_PASSWORD}":\
databaseName="${DB_NAME}":\
serverName="${IP_ADDRESS}":\
portNumber=5432:\
url=jdbc\\:postgresql\\://"${IP_ADDRESS}"\\:5432/"${DB_NAME}" \
postgres_"${APP_NAME}"_pool
./asadmin create-jdbc-resource --connectionpoolid postgres_"${APP_NAME}"_pool jdbc/postgres_"${APP_NAME}"

# Crea l'alias password per il collegamento col databse in sola lettura della BiElle
./asadmin --passwordfile "${IDE_WORKSPACE}"/"${APP_NAME}"/src/main/resources/operations/dbReadOnlyUserPassword create-password-alias ${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}
./asadmin create-jdbc-connection-pool \
--datasourceclassname com.mysql.cj.jdbc.MysqlDataSource \
--restype javax.sql.DataSource \
--property \
user="${DB_READ_ONLY_USER_NAME}":\
password=\$\{ALIAS=${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}\}:\
DatabaseName="${DB_READ_ONLY_NAME}":\
ServerName="${IP_READ_ONLY_ADDRESS}":\
portNumber="${TCP_READ_ONLY_PORT}" \
mysql_${APP_NAME}_pool
./asadmin create-jdbc-resource --connectionpoolid mysql_"${APP_NAME}"_pool jdbc/mysql_"${APP_NAME}"

./asadmin stop-domain
