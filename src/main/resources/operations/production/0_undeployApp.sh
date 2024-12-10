#!/usr/bin/env bash

# Rimuove la vecchia applicazione
${AS_HOME}/bin/asadmin undeploy ${APP_NAME}
rm -r ${AS_HOME}/glassfish/domains/${AS_DOMAIN_NAME}/generated/*

