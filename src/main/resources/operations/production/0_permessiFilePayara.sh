#!/usr/bin/env bash

# Ferma Payara e rende l'utente che gestisce il server il proprietario dei file
${AS_HOME}/bin/asadmin stop-domain ${AS_DOMAIN_NAME}
chown -R ${AS_USER_NAME}:${AS_GROUP} ${AS_DIR}

