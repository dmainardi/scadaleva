#!/bin/sh
readonly IDE_WORKSPACE=$HOME/NetBeansProjects
readonly AS_LIBFOLDER=$HOME/payara6/glassfish/domains/domain1/lib
readonly APP_NAME=scadaleva
readonly IP_ADDRESS=192.168.1.159
readonly DB_NAME="${APP_NAME}"
readonly DB_USER_NAME="${APP_NAME}"
readonly DB_USER_PASSWORD=123Stella!!!

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
./asadmin stop-domain
