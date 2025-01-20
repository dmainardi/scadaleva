#!/bin/sh

readonly AS_LIBFOLDER=$HOME/payara6/glassfish/domains/domain1/lib
readonly APP_NAME=scadaleva
readonly DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME=scadaleva-db-readyonly-user-alias

./asadmin delete-jdbc-resource jdbc/mysql_"${APP_NAME}"
./asadmin delete-jdbc-resource jdbc/mysql_readOnly_"${APP_NAME}"
./asadmin delete-jdbc-connection-pool mysql_"${APP_NAME}"_pool
./asadmin delete-jdbc-connection-pool mysql_readOnly_"${APP_NAME}"_pool
./asadmin delete-password-alias "${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}"
ls -p "${AS_LIBFOLDER}"/ | grep -v / | xargs -n1 ./asadmin remove-library
rm -r $HOME/"${APP_NAME}"/
