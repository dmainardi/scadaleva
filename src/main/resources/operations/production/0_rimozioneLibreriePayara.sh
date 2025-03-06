#!/usr/bin/env bash

# Rimuove le vecchie librerie da Payara
ls -p ${AS_HOME}/glassfish/domains/${AS_DOMAIN_NAME}/lib/ | grep -v / | xargs -n1 sh ${AS_HOME}/bin/asadmin remove-library

