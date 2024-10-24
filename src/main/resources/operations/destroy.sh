#!/bin/sh

readonly AS_LIBFOLDER=$HOME/payara6/glassfish/domains/domain1/lib
readonly APP_NAME=scadaleva

./asadmin delete-jdbc-resource jdbc/postgres_"${APP_NAME}"
./asadmin delete-jdbc-connection-pool postgres_"${APP_NAME}"_pool
ls -p "${AS_LIBFOLDER}"/ | grep -v / | xargs -n1 ./asadmin remove-library
rm -r $HOME/"${APP_NAME}"/
