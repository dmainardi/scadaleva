#!/bin/sh

readonly AS_LIBFOLDER=$HOME/payara6/glassfish/domains/domain1/lib
readonly APP_NAME=scadaleva
readonly DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME=scadaleva-db-readyonly-user-alias
readonly DB_ACCESS_READ_ONLY_FOLDER=$HOME/"${APP_NAME}"/fustellatrice

./asadmin delete-jdbc-resource jdbc/mysql_"${APP_NAME}"
./asadmin delete-jdbc-resource jdbc/mysql_readOnly_"${APP_NAME}"
./asadmin delete-jdbc-resource jdbc/access_readOnly_"${APP_NAME}"_archivioStorico
./asadmin delete-jdbc-resource jdbc/access_readOnly_"${APP_NAME}"_archivioLavoro
./asadmin delete-jdbc-connection-pool mysql_"${APP_NAME}"_pool
./asadmin delete-jdbc-connection-pool mysql_readOnly_"${APP_NAME}"_pool
./asadmin delete-jdbc-connection-pool access_readOnly_"${APP_NAME}"_archivioStorico_pool
./asadmin delete-jdbc-connection-pool access_readOnly_"${APP_NAME}"_archivioLavoro_pool
./asadmin delete-password-alias "${DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME}"
ls -p "${AS_LIBFOLDER}"/ | grep -v / | xargs -n1 ./asadmin remove-library
sudo umount -l "${DB_ACCESS_READ_ONLY_FOLDER}"
rm -r $HOME/"${APP_NAME}"/
