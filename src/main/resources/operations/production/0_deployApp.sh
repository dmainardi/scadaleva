#!/usr/bin/env bash

# Pubblica l'applicazione
${AS_HOME}/bin/asadmin deploy --skipdsfailure --contextroot "/" /home/${AS_USER_NAME}/${APP_NAME}.war

